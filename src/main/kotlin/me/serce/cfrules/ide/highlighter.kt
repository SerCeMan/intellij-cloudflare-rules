package me.serce.cfrules.ide

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.psi.tree.IElementType
import me.serce.cfrules.ide.CfRulesHighlighter.Variant.C
import me.serce.cfrules.ide.CfRulesHighlighter.Variant.EN
import me.serce.cfrules.lang.CfRulesLanguage
import me.serce.cfrules.lang.CfRulesLexer
import me.serce.cfrules.lang.psi.CfRulesElementTypes.*
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Defaults

class CfRulesHighlighterFactory : SingleLazyInstanceSyntaxHighlighterFactory() {
  override fun createHighlighter() = CfRulesHighlighter
}

object CfRulesHighlighter : SyntaxHighlighterBase() {
  override fun getHighlightingLexer() = CfRulesLexer()

  override fun getTokenHighlights(tokenType: IElementType): Array<out TextAttributesKey> {
    return pack(tokenMapping[tokenType])
  }

  private val tokenMapping: Map<IElementType, TextAttributesKey> = mapOf(
    COMMENT to CfRulesColor.LINE_COMMENT,

    LBRACE to CfRulesColor.BRACES,
    RBRACE to CfRulesColor.BRACES,
    LBRACKET to CfRulesColor.BRACKETS,
    RBRACKET to CfRulesColor.BRACKETS,
    LPAREN to CfRulesColor.PARENTHESES,
    RPAREN to CfRulesColor.PARENTHESES,

    STRINGLITERAL to CfRulesColor.STRING,
    NUMBERLITERAL to CfRulesColor.NUMBER,

    IDENTIFIER to CfRulesColor.IDENTIFIER,
    // are there better options?
    IPV4CIDR to CfRulesColor.NUMBER,
    LISTLITERAL to CfRulesColor.IDENTIFIER,
  ).plus(
    keywords().map { it to CfRulesColor.KEYWORD }
  ).plus(
    operators().map { it to CfRulesColor.OPERATION_SIGN }
  ).mapValues { it.value.textAttributesKey }

  private fun keywords() =
    allOperators()
      .filter { it.first == EN }
      .map { it.second }

  private fun operators() =
    allOperators()
      .filter { it.first == C }
      .map { it.second }

  enum class Variant {
    EN,
    C,
  }

  private fun allOperators() = setOf<Pair<Variant, IElementType>>(
    EN to NOT_EN, C to NOT_C,
    EN to AND_EN, C to AND_C,
    EN to XOR_EN, C to XOR_C,
    EN to OR_EN, C to OR_C,
    EN to EQ_EN, C to EQ_C,
    EN to NE_EN, C to NE_C,
    EN to LT_EN, C to LT_C,
    EN to LE_EN, C to LE_C,
    EN to GT_EN, C to GT_C,
    EN to GE_EN, C to GE_C,

    /* no pair */ EN to IN_EN,
    EN to MATCHES_EN, C to MATCHES_C,
    /* no pair */ EN to CONTAINS_EN,
  )
}


enum class CfRulesColor(humanName: String, default: TextAttributesKey) {
  LINE_COMMENT("Comments//Comment", Defaults.LINE_COMMENT),

  FUNCTION_CALL("Functions//Function call", Defaults.FUNCTION_CALL),

  BRACES("Other//Braces", Defaults.BRACES),
  BRACKETS("Other//Brackets", Defaults.BRACKETS),
  PARENTHESES("Other//Parentheses", Defaults.PARENTHESES),
  NUMBER("Other//Number", Defaults.NUMBER),
  STRING("Other//String", Defaults.STRING),
  KEYWORD("Other//Keyword", Defaults.KEYWORD),
  OPERATION_SIGN("Other//Operation signs", Defaults.OPERATION_SIGN),

  IDENTIFIER("Other//Identifier", Defaults.IDENTIFIER),
  ;

  val textAttributesKey = TextAttributesKey.createTextAttributesKey("me.serce.cfrules.$name", default)
  val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
}

class CfRulesColorSettingsPage : ColorSettingsPage {
  private val ATTRIBUTES: Array<AttributesDescriptor> =
    CfRulesColor.values().map { it.attributesDescriptor }.toTypedArray()
  private val ANNOTATOR_TAGS = CfRulesColor.values().associateBy({ it.name }, { it.textAttributesKey })

  private val DEMO_TEXT = """
    http.host matches "(www|api)\.example\.org"
    and not <FUNCTION_CALL>lower</FUNCTION_CALL>(http.request.uri.path) matches "/(auth|login|logut).*"
    and (
      <FUNCTION_CALL>any</FUNCTION_CALL>(http.request.uri.args.names[*] == "token") or
      ip.src in { 192.0.2.0 203.0.113.0 }
    )
    or cf.threat_score lt 10
  """.trimIndent()

  override fun getDisplayName() = CfRulesLanguage.displayName
  override fun getIcon() = CfRulesIcons.FILE_ICON
  override fun getAttributeDescriptors() = ATTRIBUTES
  override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
  override fun getHighlighter() = CfRulesHighlighter
  override fun getAdditionalHighlightingTagToDescriptorMap() = ANNOTATOR_TAGS
  override fun getDemoText() = DEMO_TEXT
}




