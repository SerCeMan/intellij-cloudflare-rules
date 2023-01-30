package me.serce.cfrules.ide

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import me.serce.cfrules.lang.psi.CfRulesElementTypes

class CfRulesQuoteTokenHandler
  : SimpleTokenSetQuoteHandler(CfRulesElementTypes.STRINGLITERAL)
