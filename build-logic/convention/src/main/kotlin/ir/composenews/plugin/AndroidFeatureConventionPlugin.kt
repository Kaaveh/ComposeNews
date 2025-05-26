package ir.composenews.plugin

import ir.composenews.utils.applyPlugins
import ir.composenews.utils.libraryExtension
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins {
                listOf(
                    vLibs.plugin("composenews-android-library").pluginId,
                    vLibs.plugin("composenews-android-library.compose").pluginId,
                    vLibs.plugin("composenews-android-hilt").pluginId,
                )
            }

            libraryExtension {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }

            dependencies {
                "implementation"(vLibs.findLibrary("hilt.navigation.compose").get())
                "androidTestImplementation"(vLibs.findLibrary("runner").get())

                "testImplementation"(project(":core:test"))
                "api"(project(":library:designsystem"))
                "api"(project(":core:base"))
            }
        }
    }
}
