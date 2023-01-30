package me.serce.cfrules.lang

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.vfs.VirtualFile
import me.serce.cfrules.ide.CfRulesIcons
import java.nio.charset.StandardCharsets

object CfRulesLanguage : Language("Cloudflare Rules", "text/cloudflare-rules") {
    override fun isCaseSensitive() = true
}

object CfRulesFileType : LanguageFileType(CfRulesLanguage) {
    object DEFAULTS {
        const val DESCRIPTION = "Cloudflare Rules file"
    }

    override fun getName() = DEFAULTS.DESCRIPTION
    override fun getDescription() = DEFAULTS.DESCRIPTION
    override fun getDefaultExtension() = "cfrule"
    override fun getIcon() = CfRulesIcons.FILE_ICON
    override fun getCharset(file: VirtualFile, content: ByteArray): String = StandardCharsets.UTF_8.name()
}
