package ir.composenews.plugin

import ir.composenews.utils.androidTestImplementation
import ir.composenews.utils.api
import ir.composenews.utils.applyPlugins
import ir.composenews.utils.implementation
import ir.composenews.utils.library
import ir.composenews.utils.libraryExtension
import ir.composenews.utils.plugin
import ir.composenews.utils.testImplementation
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
                implementation(vLibs.library("hilt-navigation-compose"))
                androidTestImplementation(vLibs.library("runner"))
                testImplementation(project(":core:test"))
                api(project(":library:designsystem"))
                api(project(":core:base"))
            }
        }
    }
}
