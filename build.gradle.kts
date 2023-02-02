import net.researchgate.release.ReleaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.intellij") version "1.12.0"
    id("org.jetbrains.grammarkit") version "2022.3"
    id("net.researchgate.release") version "3.0.2"
}

group = "me.serce"

repositories {
    mavenCentral()
}

grammarKit {
    jflexRelease.set("1.7.0-1")
    grammarKitRelease.set("2022.3")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))

    sourceSets {
        main {
            java.srcDirs("src/gen")
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("223")
        untilBuild.set("233.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    generateLexer {
        source.set("src/main/grammars/_CfRulesLexer.flex")
        targetDir.set("src/gen/me/serce/cfrules/lang/parser")
        targetClass.set("_CfRulesLexer")
        purgeOldFiles.set(true)
    }

    generateParser {
        source.set("src/main/grammars/CfRules.bnf")
        targetRoot.set("src/gen")
        pathToParser.set("me/serce/cfrules/lang/parser/CfRulesParser.java")
        pathToPsiRoot.set("me/serce/cfrules/lang/psi")
        purgeOldFiles.set(true)
    }

    withType<KotlinCompile> {
        dependsOn(generateLexer, generateParser)
    }

    configure<ReleaseExtension> {
        newVersionCommitMessage.set("[Intellij-Solidity Release] - ")
        preTagCommitMessage.set("[Intellij-Solidity Release] - pre tag commit: ")
        buildTasks.set(listOf("buildPlugin"))
        
        with(git) {
            requireBranch.set("master")
        }
    }
}
