package me.serce.cfrules.lang.completion

import me.serce.cfrules.lang.utils.CfRulesTestBase
import org.intellij.lang.annotations.Language

abstract class CfRulesCompletionTestBase : CfRulesTestBase() {
  protected fun checkCompletion(required: Set<String>, @Language("Cloudflare Rules") code: String, strict: Boolean = false) {
    InlineFile(code).withCaret()
    val variants = myFixture.completeBasic()
    checkNotNull(variants) {
      "Expected completions that contain $required, but no completions found"
    }
    val completions = variants.map { it.lookupString }.toHashSet()
    if (strict) {
      assertEquals(required.toHashSet(), completions)
    } else {
      assertTrue("$completions doesn't contain all $required", completions.containsAll(required))
    }
  }
}
