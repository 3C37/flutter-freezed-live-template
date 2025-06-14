import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

group = "com.dhh"
version = "1.0.5"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("IC", "2023.3")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        plugins("Dart:233.11799.172", "io.flutter:77.0.1", "org.jetbrains.android:233.11799.6")
    }
}

intellijPlatform {
    pluginVerification {
        ides {
            recommended()
        }
    }

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "233"
            untilBuild = "252.*"
        }

        changeNotes = """
            Version 1.0.5
            
            Minor change
    """.trimIndent()
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
