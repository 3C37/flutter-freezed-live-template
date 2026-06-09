import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    id("org.jetbrains.intellij.platform") version "2.16.0"
}

group = "com.dhh"
version = "1.0.6"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    intellijPlatform {
        intellijIdea("2026.1.3")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

intellijPlatform {
    pluginVerification {
        ides {
            create(IntelliJPlatformType.IntellijIdea, "2026.1.3")
        }
    }

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "233"
            untilBuild = "261.*"
        }

        changeNotes = """
            Version 1.0.6
            
            Update templates for Freezed 3.x syntax, fix live template top-level context persistence, enable file nesting on first startup, and remove unnecessary mandatory Android/Flutter dependencies for 2026.1 verification.
    """.trimIndent()
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks {
    withType<JavaCompile> {
        options.release.set(17)
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
