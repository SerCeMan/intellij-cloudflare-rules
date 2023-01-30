package me.serce.cfrules.ide

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import me.serce.cfrules.lang.psi.CfRulesElement
import me.serce.cfrules.lang.psi.CfRulesFunctionExpression

class CfRulesAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element is CfRulesElement) {
      highlight(element, holder)
    }
  }

  private fun highlight(element: CfRulesElement, holder: AnnotationHolder) {
    when (element) {
      is CfRulesFunctionExpression -> applyColor(holder, element.identifier, CfRulesColor.FUNCTION_CALL)
    }
  }

  private fun applyColor(holder: AnnotationHolder, element: PsiElement, color: CfRulesColor) {
    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
      .range(element)
      .textAttributes(color.textAttributesKey)
      .create()
  }

  private fun applyColor(holder: AnnotationHolder, range: TextRange, color: CfRulesColor) {
    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
      .range(range)
      .textAttributes(color.textAttributesKey)
      .create()
  }
}
