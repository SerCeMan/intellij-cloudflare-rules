package me.serce.cfrules.lang.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static me.serce.cfrules.lang.psi.CfRulesElementTypes.*;

%%

%{
  public _CfRulesLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _CfRulesLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

SPACE=[ \t\n\x0B\f\r]+
COMMENT=#.*
STRINGLITERAL=\"([^\"\\]|\\.)*\"
NUMBERLITERAL=[0-9]+(\.[0-9]*)?
IDENTIFIER=[a-zA-Z_][a-zA-Z_$0-9]*
IPV4CIDR=([0-9]{1,3}\.){3}[0-9]{1,3}("/"([0-9]|[1-2][0-9]|3[0-2]))?
IPV6CIDR=[a-fA-F0-9:]+("/"[0-9]+)?
LISTLITERAL=\$[a-zA-Z_][a-zA-Z_$0-9|.]*

%%
<YYINITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  "not"                { return NOT_EN; }
  "!"                  { return NOT_C; }
  "and"                { return AND_EN; }
  "&&"                 { return AND_C; }
  "xor"                { return XOR_EN; }
  "^^"                 { return XOR_C; }
  "or"                 { return OR_EN; }
  "||"                 { return OR_C; }
  "."                  { return DOT; }
  "eq"                 { return EQ_EN; }
  "=="                 { return EQ_C; }
  "ne"                 { return NE_EN; }
  "!="                 { return NE_C; }
  "lt"                 { return LT_EN; }
  "<"                  { return LT_C; }
  "le"                 { return LE_EN; }
  "<="                 { return LE_C; }
  "gt"                 { return GT_EN; }
  ">"                  { return GT_C; }
  "ge"                 { return GE_EN; }
  ">="                 { return GE_C; }
  "in"                 { return IN_EN; }
  "matches"            { return MATCHES_EN; }
  "~"                  { return MATCHES_C; }
  "contains"           { return CONTAINS_EN; }
  "("                  { return LPAREN; }
  ")"                  { return RPAREN; }
  "{"                  { return LBRACE; }
  "}"                  { return RBRACE; }
  "["                  { return LBRACKET; }
  "]"                  { return RBRACKET; }
  ","                  { return COMMA; }

  {SPACE}              { return SPACE; }
  {COMMENT}            { return COMMENT; }
  {STRINGLITERAL}      { return STRINGLITERAL; }
  {NUMBERLITERAL}      { return NUMBERLITERAL; }
  {IDENTIFIER}         { return IDENTIFIER; }
  {IPV4CIDR}           { return IPV4CIDR; }
  {IPV6CIDR}           { return IPV6CIDR; }
  {LISTLITERAL}        { return LISTLITERAL; }

}

[^] { return BAD_CHARACTER; }
