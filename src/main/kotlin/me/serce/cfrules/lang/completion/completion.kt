package me.serce.cfrules.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbAware
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import me.serce.cfrules.lang.psi.CfRulesFunctionExpression
import me.serce.cfrules.lang.psi.CfRulesStandardFieldExpression
import me.serce.cfrules.lang.structure.allCfFields
import me.serce.cfrules.lang.structure.allCfFunctions

class CfRulesCompletionContributor : CompletionContributor(), DumbAware {
  init {
    extend(
      CompletionType.BASIC,
      insideStandardFieldExpr(),
      FieldCompletionProvider()
    )

    extend(
      CompletionType.BASIC,
      insideFunctionExpr(),
      FunctionCompletionProvider()
    )
  }
}

class FunctionCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
    parameters: CompletionParameters,
    context: ProcessingContext,
    result: CompletionResultSet
  ) {
    // ensure it's the first identifier
    if (parameters.position.prevSibling == null) {
      val possibleFunctions = allCfFunctions
      for (function in possibleFunctions) {
        val pragmaBuilder = LookupElementBuilder
          .create(function.name)
        result.addElement(pragmaBuilder)
      }
    }
  }
}

private class FieldCompletionProvider : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
    parameters: CompletionParameters,
    context: ProcessingContext,
    result: CompletionResultSet
  ) {
    // http.referer.pa -> [http, referer]
    val parts = extractFieldParts(parameters)
    // parts could be empty as well
    val currentPartsPath = parts.joinToString(".")
    // Consider using indexed fields and a tree-like structure
    val possibleFields = allCfFields.filter { it.field.startsWith(currentPartsPath) }
    for (field in possibleFields) {
      val pragmaBuilder = LookupElementBuilder
        .create(field.field.let {
          if (currentPartsPath.isEmpty()) {
            it
          } else {
            it.replace("$currentPartsPath.", "")
          }
        })
        .withTailText(" " + field.field, true)
      result.addElement(pragmaBuilder)
    }
  }

  private fun extractFieldParts(parameters: CompletionParameters): List<String> {
    val parts = arrayListOf<String>()
    var current: PsiElement? = parameters.position
    while (current != null) {
      current = current.prevSibling;
      if (current == null || current.text == ".") {
        continue
      }
      parts.add(current.text)
    }
    return parts.reversed()
  }
}

private fun insideStandardFieldExpr(): PsiElementPattern.Capture<PsiElement> =
  psiElement<PsiElement>()
    .inside(CfRulesStandardFieldExpression::class.java)

private fun insideFunctionExpr(): PsiElementPattern.Capture<PsiElement> =
  psiElement<PsiElement>()
    .inside(CfRulesFunctionExpression::class.java)


private inline fun <reified I : PsiElement> psiElement(): PsiElementPattern.Capture<I> {
  return PlatformPatterns.psiElement(I::class.java)
}
