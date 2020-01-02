/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.cfg;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@RunWith(JUnit3RunnerWithInners.class)
public class DataFlowTestGenerated extends AbstractDataFlowTest {
    @TestMetadata("compiler/testData/cfg-variables")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Cfg_variables extends AbstractDataFlowTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInCfg_variables() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/cfg-variables"), Pattern.compile("^(.+)\\.kt$"), null, true);
        }

        @TestMetadata("compiler/testData/cfg-variables/basic")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Basic extends AbstractDataFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInBasic() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/cfg-variables/basic"), Pattern.compile("^(.+)\\.kt$"), null, true);
            }

            @TestMetadata("ExhaustiveInitialization.kt")
            public void testExhaustiveInitialization() throws Exception {
                runTest("compiler/testData/cfg-variables/basic/ExhaustiveInitialization.kt");
            }

            @TestMetadata("IfWithUninitialized.kt")
            public void testIfWithUninitialized() throws Exception {
                runTest("compiler/testData/cfg-variables/basic/IfWithUninitialized.kt");
            }

            @TestMetadata("InitializedNotDeclared.kt")
            public void testInitializedNotDeclared() throws Exception {
                runTest("compiler/testData/cfg-variables/basic/InitializedNotDeclared.kt");
            }

            @TestMetadata("UsageInFunctionLiteral.kt")
            public void testUsageInFunctionLiteral() throws Exception {
                runTest("compiler/testData/cfg-variables/basic/UsageInFunctionLiteral.kt");
            }

            @TestMetadata("UseUninitializedInLambda.kt")
            public void testUseUninitializedInLambda() throws Exception {
                runTest("compiler/testData/cfg-variables/basic/UseUninitializedInLambda.kt");
            }

            @TestMetadata("VariablesInitialization.kt")
            public void testVariablesInitialization() throws Exception {
                runTest("compiler/testData/cfg-variables/basic/VariablesInitialization.kt");
            }

            @TestMetadata("VariablesUsage.kt")
            public void testVariablesUsage() throws Exception {
                runTest("compiler/testData/cfg-variables/basic/VariablesUsage.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg-variables/bugs")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Bugs extends AbstractDataFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInBugs() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/cfg-variables/bugs"), Pattern.compile("^(.+)\\.kt$"), null, true);
            }

            @TestMetadata("doWhileAssignment.kt")
            public void testDoWhileAssignment() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/doWhileAssignment.kt");
            }

            @TestMetadata("doWhileNotDefined.kt")
            public void testDoWhileNotDefined() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/doWhileNotDefined.kt");
            }

            @TestMetadata("initializationInLocalClass.kt")
            public void testInitializationInLocalClass() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/initializationInLocalClass.kt");
            }

            @TestMetadata("kt10243.kt")
            public void testKt10243() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/kt10243.kt");
            }

            @TestMetadata("kt4764.kt")
            public void testKt4764() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/kt4764.kt");
            }

            @TestMetadata("kt5469.kt")
            public void testKt5469() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/kt5469.kt");
            }

            @TestMetadata("kt9825.kt")
            public void testKt9825() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/kt9825.kt");
            }

            @TestMetadata("localObjectInConstructor.kt")
            public void testLocalObjectInConstructor() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/localObjectInConstructor.kt");
            }

            @TestMetadata("referenceToPropertyInitializer.kt")
            public void testReferenceToPropertyInitializer() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/referenceToPropertyInitializer.kt");
            }

            @TestMetadata("varInitializationInIf.kt")
            public void testVarInitializationInIf() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/varInitializationInIf.kt");
            }

            @TestMetadata("varInitializationInIfInCycle.kt")
            public void testVarInitializationInIfInCycle() throws Exception {
                runTest("compiler/testData/cfg-variables/bugs/varInitializationInIfInCycle.kt");
            }
        }

        @TestMetadata("compiler/testData/cfg-variables/lexicalScopes")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class LexicalScopes extends AbstractDataFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInLexicalScopes() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/cfg-variables/lexicalScopes"), Pattern.compile("^(.+)\\.kt$"), null, true);
            }

            @TestMetadata("doWhileScope.kt")
            public void testDoWhileScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/doWhileScope.kt");
            }

            @TestMetadata("forScope.kt")
            public void testForScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/forScope.kt");
            }

            @TestMetadata("functionLiteralScope.kt")
            public void testFunctionLiteralScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/functionLiteralScope.kt");
            }

            @TestMetadata("ifScope.kt")
            public void testIfScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/ifScope.kt");
            }

            @TestMetadata("localClass.kt")
            public void testLocalClass() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/localClass.kt");
            }

            @TestMetadata("localFunctionScope.kt")
            public void testLocalFunctionScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/localFunctionScope.kt");
            }

            @TestMetadata("localFunctionScopeWithoutBody.kt")
            public void testLocalFunctionScopeWithoutBody() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/localFunctionScopeWithoutBody.kt");
            }

            @TestMetadata("localObject.kt")
            public void testLocalObject() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/localObject.kt");
            }

            @TestMetadata("objectLiteralScope.kt")
            public void testObjectLiteralScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/objectLiteralScope.kt");
            }

            @TestMetadata("propertyAccessorScope.kt")
            public void testPropertyAccessorScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/propertyAccessorScope.kt");
            }

            @TestMetadata("tryScope.kt")
            public void testTryScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/tryScope.kt");
            }

            @TestMetadata("whileScope.kt")
            public void testWhileScope() throws Exception {
                runTest("compiler/testData/cfg-variables/lexicalScopes/whileScope.kt");
            }
        }
    }

    @TestMetadata("compiler/testData/cfgVariablesWithStdLib")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CfgVariablesWithStdLib extends AbstractDataFlowTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTestWithStdLib, this, testDataFilePath);
        }

        public void testAllFilesPresentInCfgVariablesWithStdLib() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/cfgVariablesWithStdLib"), Pattern.compile("^(.+)\\.kt$"), null, true);
        }

        @TestMetadata("compiler/testData/cfgVariablesWithStdLib/contracts")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Contracts extends AbstractDataFlowTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTestWithStdLib, this, testDataFilePath);
            }

            public void testAllFilesPresentInContracts() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/cfgVariablesWithStdLib/contracts"), Pattern.compile("^(.+)\\.kt$"), null, true);
            }

            @TestMetadata("breakContinuesInInlinedLambda.kt")
            public void testBreakContinuesInInlinedLambda() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/breakContinuesInInlinedLambda.kt");
            }

            @TestMetadata("inlinedLambdaAlwaysThrows.kt")
            public void testInlinedLambdaAlwaysThrows() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/inlinedLambdaAlwaysThrows.kt");
            }

            @TestMetadata("irrelevantUnknownClosure.kt")
            public void testIrrelevantUnknownClosure() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/irrelevantUnknownClosure.kt");
            }

            @TestMetadata("nestedTryCatchFinally.kt")
            public void testNestedTryCatchFinally() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/nestedTryCatchFinally.kt");
            }

            @TestMetadata("nestedTryCatchs.kt")
            public void testNestedTryCatchs() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/nestedTryCatchs.kt");
            }

            @TestMetadata("nonReturningInlinedLambda.kt")
            public void testNonReturningInlinedLambda() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/nonReturningInlinedLambda.kt");
            }

            @TestMetadata("returnsAndCalls.kt")
            public void testReturnsAndCalls() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/returnsAndCalls.kt");
            }

            @TestMetadata("throwIfNotCalled.kt")
            public void testThrowIfNotCalled() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/throwIfNotCalled.kt");
            }

            @TestMetadata("tryCatch.kt")
            public void testTryCatch() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/tryCatch.kt");
            }

            @TestMetadata("tryCatchFinally.kt")
            public void testTryCatchFinally() throws Exception {
                runTest("compiler/testData/cfgVariablesWithStdLib/contracts/tryCatchFinally.kt");
            }
        }
    }
}
