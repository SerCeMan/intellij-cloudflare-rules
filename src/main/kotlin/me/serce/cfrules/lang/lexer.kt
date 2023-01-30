package me.serce.cfrules.lang

import com.intellij.lexer.FlexAdapter
import me.serce.cfrules.lang.parser._CfRulesLexer

class CfRulesLexer : FlexAdapter(_CfRulesLexer())
