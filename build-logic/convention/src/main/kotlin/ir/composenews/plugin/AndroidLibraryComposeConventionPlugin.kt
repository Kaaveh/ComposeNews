package ir.composenews.plugin

import ir.composenews.utils.libraryExtension
import ir.composenews.conv.configureAndroidCompose
import ir.composenews.utils.applyPlugins
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins {
                listOf(
                    vLibs.plugin("android-library").pluginId,
                    vLibs.plugin("compose").pluginId,
                )
            }

            libraryExtension {
                configureAndroidCompose(this)
            }
        }
    }
}
