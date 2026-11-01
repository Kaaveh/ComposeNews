package ir.composenews.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** iOS-only KMP module, so no Android target: the `shared` umbrella framework. */
class KmpIosConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        applyPlugin("kotlin-multiplatform")
        applyCodeQualityPlugins()
        extensions.configure<KotlinMultiplatformExtension> {
            iosTargets()
            compilerOptions.configureKotlin()
        }
        detektKmpSources()
    }
}
