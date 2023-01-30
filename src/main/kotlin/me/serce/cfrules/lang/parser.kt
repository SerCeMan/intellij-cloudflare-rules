package me.serce.cfrules.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.LanguageUtil
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import me.serce.cfrules.lang.parser.CfRulesParser
import me.serce.cfrules.lang.psi.CfRulesElementTypes
import me.serce.cfrules.lang.stubs.CfRulesFileStub

class CfRulesParserDefinition : ParserDefinition {
  override fun createParser(project: Project?): PsiParser = CfRulesParser()

  override fun createFile(viewProvider: FileViewProvider): PsiFile = CfRulesFile(viewProvider)

  override fun spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements =
    LanguageUtil.canStickTokensTogetherByLexer(left, right, CfRulesLexer())

  override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

  override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

  override fun getCommentTokens(): TokenSet = COMMENTS

  override fun getFileNodeType(): IFileElementType = CfRulesFileStub.Type

  override fun createLexer(project: Project?): Lexer = CfRulesLexer()

  override fun createElement(node: ASTNode?): PsiElement = CfRulesElementTypes.Factory.createElement(node)

  companion object {
    val WHITE_SPACES: TokenSet = TokenSet.create(TokenType.WHITE_SPACE)
    val COMMENTS: TokenSet = TokenSet.create(CfRulesElementTypes.COMMENT)
//    val BINARY_OPERATORS: TokenSet = TokenSet.create(
//      PLUS, MINUS, MULT, DIV, EXPONENT,
//      ASSIGN, TO, EQ, NEQ,
//      PLUS_ASSIGN, MINUS_ASSIGN, MULT_ASSIGN, DIV_ASSIGN, OR_ASSIGN, XOR_ASSIGN, AND_ASSIGN, LSHIFT_ASSIGN, RSHIFT_ASSIGN, PERCENT_ASSIGN,
//      LESS, LESSEQ, MORE, MOREEQ, CARET, AND, ANDAND, OR, OROR,
//      PERCENT, LSHIFT, RSHIFT, LEFT_ASSEMBLY, RIGHT_ASSEMBLY
//    )
//    val CONTROL_STRUCTURES: TokenSet = TokenSet.create(
//      IF, ELSE, WHILE, FOR, DO
//    )
  }
}
