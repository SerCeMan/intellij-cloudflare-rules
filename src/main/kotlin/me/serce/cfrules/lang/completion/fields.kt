package me.serce.cfrules.lang.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbAware
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import me.serce.cfrules.lang.psi.CfRulesStandardFieldExpression

private data class Field(val field: String, val description: String) {
  fun fieldName() = field.split(".").last()
}

// taken from https://developers.cloudflare.com/ruleset-engine/rules-language/fields/
private val allFields: List<Field> = listOf(
  Field(
    "http.cookie",
    """
    Represents the entire cookie as a string.
    
    Example value:
    session=8521F670545D7865F79C3D7BEDC29CCE;-background=light
    """.trimIndent(),
  ),
  Field(
    "http.host",
    """
    Represents the host name used in the full request URI.
    
    Example value:
    www.example.org
    """.trimIndent(),
  ),
  Field(
    "http.referer",
    """
    Represents the HTTP Referer request header, which contains the address of the web page that linked to the currently requested page.
    
    Example value:
    Referer: https://developer.example.org/en-US/docs/Web/JavaScript
    """.trimIndent(),
  ),
  Field(
    "http.request.full_uri",
    """
    Represents the full URI as received by the web server (does not include #fragment, which is not sent to web servers).
    
    Example value:
    https://www.example.org/articles/index?section=539061&expand=comments
    """.trimIndent(),
  ),
)

private val indexedFields: Map<String, List<Field>> =
  allFields.groupBy { it.field.split(".").dropLast(1).joinToString(".") }

class CfRulesCompletionContributor : CompletionContributor(), DumbAware {
  init {
    extend(CompletionType.BASIC, insideStandardFieldExpr(), object : CompletionProvider<CompletionParameters>() {
      override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
      ) {
        val parts = extractFieldParts(parameters)
        if (parts.isEmpty()) {
          return
        }
        val possibleFields = indexedFields[parts.joinToString(".")] ?: emptyList()
        for (field in possibleFields) {
          val pragmaBuilder = LookupElementBuilder
            .create(field.fieldName())
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
    })
  }
}

fun insideStandardFieldExpr(): PsiElementPattern.Capture<PsiElement> = psiElement<PsiElement>()
  .inside(CfRulesStandardFieldExpression::class.java)

private inline fun <reified I : PsiElement> psiElement(): PsiElementPattern.Capture<I> {
  return PlatformPatterns.psiElement(I::class.java)
}
