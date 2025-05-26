import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "ir.composenews.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.ktlint.kotlinter)
    compileOnly(libs.compose.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = libs.plugins.composenews.android.library.asProvider().get().pluginId
            implementationClass = "ir.composenews.plugin.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.composenews.android.library.compose.get().pluginId
            implementationClass = "ir.composenews.plugin.AndroidLibraryComposeConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.composenews.android.application.asProvider().get().pluginId
            implementationClass = "ir.composenews.plugin.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.composenews.android.application.compose.get().pluginId
            implementationClass = "ir.composenews.plugin.AndroidApplicationComposeConventionPlugin"
        }
        register("androidHilt") {
            id = libs.plugins.composenews.android.hilt.get().pluginId
            implementationClass = "ir.composenews.plugin.HiltConventionPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.composenews.android.feature.get().pluginId
            implementationClass = "ir.composenews.plugin.AndroidFeatureConventionPlugin"
        }
        register("androidDetekt") {
            id = libs.plugins.composenews.android.detekt.get().pluginId
            implementationClass = "ir.composenews.plugin.AndroidDetektConventionPlugin"
        }
        register("androidKtlint") {
            id = libs.plugins.composenews.android.ktlint.get().pluginId
            implementationClass = "ir.composenews.plugin.AndroidKotlinterConventionPlugin"
        }
    }
}
