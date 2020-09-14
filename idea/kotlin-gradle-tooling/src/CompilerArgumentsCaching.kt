/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle

import java.io.File
import java.io.Serializable

typealias CachedArgumentIdType = Int
typealias ClasspathArgumentCacheIdType = Array<Int>

interface CachedArgsInfo : Serializable {
    val currentCachedArgumentIds: Array<CachedArgumentIdType>
    val currentCachedClasspathArgumentIds: Array<ClasspathArgumentCacheIdType>
    val defaultCachedArgumentsIds: Array<CachedArgumentIdType>
    val dependencyClasspathCacheIds: Array<ClasspathArgumentCacheIdType>
}

class CachedArgsInfoImpl(
    override val currentCachedArgumentIds: Array<CachedArgumentIdType>,
    override val currentCachedClasspathArgumentIds: Array<ClasspathArgumentCacheIdType>,
    override val defaultCachedArgumentsIds: Array<CachedArgumentIdType>,
    override val dependencyClasspathCacheIds: Array<ClasspathArgumentCacheIdType>
) : CachedArgsInfo {
    constructor(cachedArgsInfo: CachedArgsInfo) : this(
        arrayOf(*cachedArgsInfo.currentCachedArgumentIds),
        arrayOf(*cachedArgsInfo.currentCachedClasspathArgumentIds),
        arrayOf(*cachedArgsInfo.defaultCachedArgumentsIds),
        arrayOf(*cachedArgsInfo.dependencyClasspathCacheIds)
    )
}

typealias CachedCompilerArgumentBySourceSet = Map<String, CachedArgsInfo>

/**
 * Creates deep copy in order to avoid holding links to Proxy objects created by gradle tooling api
 */
fun CachedCompilerArgumentBySourceSet.deepCopy(): CachedCompilerArgumentBySourceSet {
    val result = HashMap<String, CachedArgsInfo>()
    this.forEach { key, value -> result[key] = CachedArgsInfoImpl(value) }
    return result
}

interface ICompilerArgumentsMapper : Serializable {
    fun getArgumentCache(argument: String): Int?
    fun cacheCommonArgument(commonArgument: String): Int
    fun getCommonArgument(id: Int): String
    fun cacheClasspathArgument(classpathArgument: String): Array<Int>
    fun getClasspathArgument(ids: Array<Int>): String
    fun clear()
    fun copyCache(): Map<Int, String>
}

open class CompilerArgumentsMapper(val initialId: Int = 0) : ICompilerArgumentsMapper {

    constructor(otherMapper: CompilerArgumentsMapper) : this(otherMapper.initialId) {
        idToCompilerArguments.putAll(otherMapper.idToCompilerArguments)
        compilerArgumentToId.putAll(otherMapper.compilerArgumentToId)
        nextId = otherMapper.nextId
    }

    protected var nextId = initialId
    protected val idToCompilerArguments: MutableMap<Int, String> = mutableMapOf()
    protected val compilerArgumentToId: MutableMap<String, Int> = mutableMapOf()

    override fun getArgumentCache(argument: String): Int? = compilerArgumentToId[argument]

    override fun cacheCommonArgument(commonArgument: String): Int = getArgumentCache(commonArgument)
        ?: run {
            val index = nextId++
            idToCompilerArguments[index] = commonArgument
            compilerArgumentToId[commonArgument] = index
            index
        }

    override fun getCommonArgument(id: Int): String = idToCompilerArguments[id]!!

    override fun cacheClasspathArgument(classpathArgument: String): Array<Int> =
        classpathArgument.split(File.pathSeparator).map { cacheCommonArgument(it) }.toTypedArray()

    override fun getClasspathArgument(ids: Array<Int>): String = ids.joinToString(File.pathSeparator) { getCommonArgument(it) }
    override fun clear() {
        idToCompilerArguments.clear()
        compilerArgumentToId.clear()
        nextId = initialId
    }

    override fun copyCache(): Map<Int, String> = HashMap(idToCompilerArguments)
}

interface IDetachableMapper : ICompilerArgumentsMapper {
    val masterMapper: ICompilerArgumentsMapper
    fun detach(): ICompilerArgumentsMapper
}

class DetachableCompilerArgumentsMapper(override val masterMapper: ICompilerArgumentsMapper) : CompilerArgumentsMapper(),
    IDetachableMapper {
    override fun cacheCommonArgument(commonArgument: String): Int =
        masterMapper.getArgumentCache(commonArgument) ?: masterMapper.cacheCommonArgument(commonArgument).also {
            idToCompilerArguments[it] = commonArgument
            compilerArgumentToId[commonArgument] = it
        }

    override fun detach(): ICompilerArgumentsMapper = CompilerArgumentsMapper(this)
}

interface IWithCheckoutMapper : ICompilerArgumentsMapper {
    fun checkoutMapper(): IDetachableMapper
}

class CompilerArgumentsMapperWithCheckout : CompilerArgumentsMapper(), IWithCheckoutMapper {
    override fun checkoutMapper(): IDetachableMapper = DetachableCompilerArgumentsMapper(this)
}

interface IWithMergeMapper : ICompilerArgumentsMapper {
    fun mergeMapper(mapper: ICompilerArgumentsMapper)
}

class CompilerArgumentsMapperWithMerge : CompilerArgumentsMapper(), IWithMergeMapper {

    override fun mergeMapper(mapper: ICompilerArgumentsMapper) {
        mapper.copyCache().forEach { (id, arg) ->
            idToCompilerArguments[id] = arg
            compilerArgumentToId[arg] = id
        }
    }
}

data class CompilerArgumentMappersContainer @JvmOverloads constructor(
    val projectCompilerArgumentsMapper: CompilerArgumentsMapperWithMerge = CompilerArgumentsMapperWithMerge(),
    val projectMppCompilerArgumentsMapper: CompilerArgumentsMapperWithMerge = CompilerArgumentsMapperWithMerge()
) {
    @JvmOverloads
    fun mergeArgumentsFromMapper(mapper: ICompilerArgumentsMapper, isMpp: Boolean = false) {
        if (isMpp) projectMppCompilerArgumentsMapper.mergeMapper(mapper) else projectCompilerArgumentsMapper.mergeMapper(mapper)
    }
}
