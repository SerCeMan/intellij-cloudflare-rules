package me.serce.cfrules.lang.utils

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.intellij.lang.annotations.Language

abstract class CfRulesTestBase : CfRulesLightPlatformCodeInsightFixtureTestCase() {
  inner class InlineFile(@Language("Cloudflare Rules") private val code: String, val name: String = "ctr.cfrule") {
    val psiFile: PsiFile
    private val hasCaretMarker = "/*caret*/" in code

    init {
      psiFile = myFixture.configureByText(name, replaceCaretMarker(code))
    }

    fun withCaret() {
      check(hasCaretMarker) {
        "Please, add `/*caret*/` marker to\n$code"
      }
    }
  }

  protected val fileName: String
    get() = "${getTestName(true)}.cfrule"

  val fixture: CodeInsightTestFixture
    get() = super.myFixture

  protected fun replaceCaretMarker(text: String) = text.replace("/*caret*/", "<caret>")

  inline fun <reified T : PsiElement> findElementAndDataInEditor(marker: String = "^"): Pair<T, String> {
    val caretMarker = "//$marker"
    val (elementAtMarker, data) = run {
      val text = fixture.file.text
      val markerOffset = text.indexOf(caretMarker)
      check(markerOffset != -1) { "No `$marker` marker:\n$text" }
      check(text.indexOf(caretMarker, startIndex = markerOffset + 1) == -1) {
        "More than one `$marker` marker:\n$text"
      }

      val data = text.drop(markerOffset).removePrefix(caretMarker).takeWhile { it != '\n' }.trim()
      val markerPosition = fixture.editor.offsetToLogicalPosition(markerOffset + caretMarker.length - 1)
      val previousLine = LogicalPosition(markerPosition.line - 1, markerPosition.column)
      val elementOffset = fixture.editor.logicalPositionToOffset(previousLine)
      fixture.file.findElementAt(elementOffset)!! to data
    }
    val element = elementAtMarker.parentOfType<T>(strict = false)
      ?: error("No ${T::class.java.simpleName} at ${elementAtMarker.text}")
    return element to data
  }

  inline fun <reified T : PsiElement> PsiElement.parentOfType(strict: Boolean = true, minStartOffset: Int = -1): T? =
    PsiTreeUtil.getParentOfType(this, T::class.java, strict, minStartOffset)

  inline fun <reified T : PsiElement> findElementInEditor(marker: String = "^"): T {
    val (element, data) = findElementAndDataInEditor<T>(marker)
    check(data.isEmpty()) { "Did not expect marker data" }
    return element
  }

  protected fun checkByFile(ignoreTrailingWhitespace: Boolean = true, action: () -> Unit) {
    val (before, after) = (fileName to fileName.replace(".cfrule", "After.cfrule"))
    myFixture.configureByFile(before)
    action()
    myFixture.checkResultByFile(after, ignoreTrailingWhitespace)
  }

  protected fun checkResult(@Language("Cloudflare Rules") text: String) {
    myFixture.checkResult(text)
  }
}
