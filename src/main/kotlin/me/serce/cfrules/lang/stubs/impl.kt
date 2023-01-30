package me.serce.cfrules.lang.stubs

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import me.serce.cfrules.lang.CfRulesFile
import me.serce.cfrules.lang.CfRulesLanguage

class CfRulesFileStub(file: CfRulesFile?) : PsiFileStubImpl<CfRulesFile>(file) {
    override fun getType() = Type

    object Type : IStubFileElementType<CfRulesFileStub>(CfRulesLanguage) {
        // bump version every time stub tree changes
        override fun getStubVersion() = 1

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile) = CfRulesFileStub(file as CfRulesFile)
        }

        override fun serialize(stub: CfRulesFileStub, dataStream: StubOutputStream) {}

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) = CfRulesFileStub(null)

        override fun getExternalId(): String = "CfRules.file"
    }
}
