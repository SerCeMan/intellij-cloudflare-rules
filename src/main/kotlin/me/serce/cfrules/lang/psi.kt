package me.serce.cfrules.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.IElementType

class CfRulesFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CfRulesLanguage) {
    override fun getFileType(): FileType = CfRulesFileType
    override fun toString(): String = "Cloudflare Rules File"
}

class CfRulesElementType(val name: String) : IElementType(name, CfRulesLanguage)
