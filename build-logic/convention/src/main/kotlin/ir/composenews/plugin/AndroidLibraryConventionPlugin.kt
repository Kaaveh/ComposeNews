package ir.composenews.plugin

import ir.composenews.utils.libraryExtension
import ir.composenews.conv.configureKotlinAndroid
import ir.composenews.utils.androidTestImplementation
import ir.composenews.utils.applyPlugins
import ir.composenews.utils.plugin
import ir.composenews.utils.testImplementation
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins {
                listOf(
                    vLibs.plugin("android-library").pluginId,
                    vLibs.plugin("kotlin-android").pluginId,
                    vLibs.plugin("composenews-android-detekt").pluginId,
                    vLibs.plugin("composenews-android-ktlint").pluginId,
                )
            }

            libraryExtension {
                configureKotlinAndroid(this)
            }

            dependencies {
                androidTestImplementation(kotlin("test"))
                testImplementation(kotlin("test"))
            }
        }
    }
}
