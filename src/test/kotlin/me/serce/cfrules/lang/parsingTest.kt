package me.serce.cfrules.lang

import com.intellij.core.CoreApplicationEnvironment
import com.intellij.lang.LanguageExtensionPoint
import com.intellij.testFramework.ParsingTestCase

abstract class CfRulesParsingTestBase(baseDir: String) :
    ParsingTestCase(baseDir, "cfrule", true, CfRulesParserDefinition()) {

    override fun setUp() {
        super.setUp()
        CoreApplicationEnvironment.registerExtensionPoint(
            application.extensionArea,
            "com.intellij.lang.braceMatcher",
            LanguageExtensionPoint::class.java
        )
    }

    override fun getTestDataPath() = "src/test/resources"
}

class CfRulesParsingTest : CfRulesParsingTestBase("fixtures/parser") {
    fun testComments() = doTest(true)
    fun testSample() = doTest(true)
    fun testIPv6() = doTest(true)
}
