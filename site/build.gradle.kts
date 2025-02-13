import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.spotless)
}

group = "todos"
version = "1.0.0"

kotlin {
    configAsKobwebApplication(includeServer = true)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }

        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
        }

        jvmMain.dependencies {
            implementation(libs.postgresql)
            implementation(libs.bundles.exposed)
            compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime
        }
    }
}

spotless {
    kotlin {
        ktlint().setEditorConfigPath(project.file(".editorconfig"))
    }
}
