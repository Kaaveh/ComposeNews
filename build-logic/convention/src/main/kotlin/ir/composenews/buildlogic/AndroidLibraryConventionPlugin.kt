package ir.composenews.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/** Android-only library without Compose. Modules with UI use [AndroidLibraryComposeConventionPlugin]. */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        applyPlugin("android-library")
        applyPlugin("kotlin-android")
        applyCodeQualityPlugins()

        extensions.configure<LibraryExtension> { configureAndroid(this) }
        extensions.configure<KotlinAndroidProjectExtension> { compilerOptions.configureKotlinJvm() }
    }
}
