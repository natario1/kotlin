/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.backend.jvm.intrinsics

import com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.backend.jvm.JvmBackendContext
import org.jetbrains.kotlin.backend.jvm.codegen.*
import org.jetbrains.kotlin.backend.jvm.ir.isSmartcastFromHigherThanNullable
import org.jetbrains.kotlin.builtins.PrimitiveType
import org.jetbrains.kotlin.codegen.AsmUtil
import org.jetbrains.kotlin.codegen.AsmUtil.comparisonOperandType
import org.jetbrains.kotlin.codegen.BranchedValue
import org.jetbrains.kotlin.codegen.NumberCompare
import org.jetbrains.kotlin.codegen.ObjectCompare
import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.lexer.KtSingleValueToken
import org.jetbrains.kotlin.resolve.jvm.JvmPrimitiveType
import org.jetbrains.kotlin.resolve.jvm.jvmSignature.JvmMethodSignature
import org.jetbrains.org.objectweb.asm.Label
import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter

object CompareTo : IntrinsicMethod() {
    private fun genInvoke(type: Type?, v: InstructionAdapter) {
        when (type) {
            Type.CHAR_TYPE, Type.BYTE_TYPE, Type.SHORT_TYPE, Type.INT_TYPE ->
                v.invokestatic(
                    IrIntrinsicMethods.INTRINSICS_CLASS_NAME,
                    "compare",
                    "(II)I",
                    false
                )
            Type.LONG_TYPE -> v.invokestatic(IrIntrinsicMethods.INTRINSICS_CLASS_NAME, "compare", "(JJ)I", false)
            Type.FLOAT_TYPE -> v.invokestatic("java/lang/Float", "compare", "(FF)I", false)
            Type.DOUBLE_TYPE -> v.invokestatic("java/lang/Double", "compare", "(DD)I", false)
            else -> throw UnsupportedOperationException()
        }
    }

    override fun toCallable(
        expression: IrFunctionAccessExpression,
        signature: JvmMethodSignature,
        context: JvmBackendContext
    ): IrIntrinsicFunction {
        val parameterType = comparisonOperandType(
            expressionType(expression.dispatchReceiver ?: expression.extensionReceiver!!, context),
            signature.valueParameters.single().asmType
        )
        return IrIntrinsicFunction.create(expression, signature, context, listOf(parameterType, parameterType)) {
            genInvoke(parameterType, it)
        }
    }
}

class IntegerZeroComparison(val op: IElementType, val a: MaterialValue) : BooleanValue(a.codegen) {
    override fun jumpIfFalse(target: Label) {
        mv.visitJumpInsn(Opcodes.IFNE, target)
    }

    override fun jumpIfTrue(target: Label) {
        mv.visitJumpInsn(Opcodes.IFEQ, target)
    }

    override fun discard() {
        a.discard()
    }
}

class BooleanComparison(val op: IElementType, val a: MaterialValue, val b: MaterialValue) : BooleanValue(a.codegen) {
    override fun jumpIfFalse(target: Label) {
        // TODO 1. get rid of the dependency; 2. take `b.type` into account.
        val opcode = if (a.type.sort == Type.OBJECT)
            ObjectCompare.getObjectCompareOpcode(op)
        else
            NumberCompare.patchOpcode(NumberCompare.getNumberCompareOpcode(op), mv, op, a.type)
        mv.visitJumpInsn(opcode, target)
    }

    override fun jumpIfTrue(target: Label) {
        val opcode = if (a.type.sort == Type.OBJECT)
            BranchedValue.negatedOperations[ObjectCompare.getObjectCompareOpcode(op)]!!
        else
            NumberCompare.patchOpcode(BranchedValue.negatedOperations[NumberCompare.getNumberCompareOpcode(op)]!!, mv, op, a.type)
        mv.visitJumpInsn(opcode, target)
    }

    override fun discard() {
        b.discard()
        a.discard()
    }
}


class NonIEEE754FloatComparison(val op: IElementType, val a: MaterialValue, val b: MaterialValue) : BooleanValue(a.codegen) {
    private val numberCompareOpcode = NumberCompare.getNumberCompareOpcode(op)

    private fun invokeStaticComparison(type: Type) {
        when (type) {
            Type.FLOAT_TYPE -> mv.invokestatic("java/lang/Float", "compare", "(FF)I", false)
            Type.DOUBLE_TYPE -> mv.invokestatic("java/lang/Double", "compare", "(DD)I", false)
            else -> throw UnsupportedOperationException()
        }
    }

    override fun jumpIfFalse(target: Label) {
        invokeStaticComparison(a.type)
        mv.visitJumpInsn(numberCompareOpcode, target)
    }

    override fun jumpIfTrue(target: Label) {
        invokeStaticComparison(a.type)
        mv.visitJumpInsn(BranchedValue.negatedOperations[numberCompareOpcode]!!, target)
    }

    override fun discard() {
        b.discard()
        a.discard()
    }
}

class PrimitiveToObjectComparison(
    val op: IElementType,
    private val boxedValue: MaterialValue,
    private val useNullCheck: Boolean,
    private val primitiveType: Type,
    private val loadOther: () -> MaterialValue,
) : BooleanValue(boxedValue.codegen) {

    override fun jumpIfFalse(target: Label) {
        val compareLabel = Label()
        mv.dup()
        if (useNullCheck) {
            mv.ifnonnull(compareLabel)
        } else {
            mv.instanceOf(AsmUtil.boxType(primitiveType))
            mv.ifne(compareLabel)
        }
        mv.pop()
        mv.goTo(target)
        mv.mark(compareLabel)
        val unboxedValue = boxedValue.materializedAt(primitiveType, boxedValue.irType)
        BooleanComparison(op, unboxedValue, loadOther()).jumpIfFalse(target)
    }

    override fun jumpIfTrue(target: Label) {
        val compareLabel = Label()
        val endLabel = Label()
        mv.dup()
        if (useNullCheck) {
            mv.ifnonnull(compareLabel)
        } else {
            mv.instanceOf(AsmUtil.boxType(primitiveType))
            mv.ifne(compareLabel)
        }
        mv.pop()
        mv.goTo(endLabel)
        mv.mark(compareLabel)
        val unboxedValue = boxedValue.materializedAt(primitiveType, boxedValue.irType)
        BooleanComparison(op, unboxedValue, loadOther()).jumpIfTrue(target)
        mv.mark(endLabel)
    }

    override fun discard() {
        boxedValue.discard()
    }
}

class PrimitiveComparison(
    private val primitiveNumberType: PrimitiveType,
    private val operatorToken: KtSingleValueToken
) : IntrinsicMethod() {
    override fun invoke(expression: IrFunctionAccessExpression, codegen: ExpressionCodegen, data: BlockInfo): PromisedValue? {
        val parameterType = Type.getType(JvmPrimitiveType.get(primitiveNumberType).desc)
        val (left, right) = expression.receiverAndArgs()
        val a = left.accept(codegen, data).materializedAt(parameterType, left.type)
        val b = right.accept(codegen, data).materializedAt(parameterType, right.type)

        val useNonIEEE754Comparison =
            !codegen.context.state.languageVersionSettings.supportsFeature(LanguageFeature.ProperIeee754Comparisons)
                    && (parameterType == Type.FLOAT_TYPE || parameterType == Type.DOUBLE_TYPE)
                    && (left.isSmartcastFromHigherThanNullable(codegen.context) || right.isSmartcastFromHigherThanNullable(codegen.context))

        return if (useNonIEEE754Comparison) {
            NonIEEE754FloatComparison(operatorToken, a, b)
        } else {
            BooleanComparison(operatorToken, a, b)
        }
    }
}

