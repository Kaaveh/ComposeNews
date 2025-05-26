package ir.composenews.plugin

import ir.composenews.utils.applicationExtension
import ir.composenews.conv.configureAndroidCompose
import ir.composenews.utils.applyPlugins
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins {
                listOf(
                    vLibs.plugin("android-application").pluginId
                )
            }

            applicationExtension {
                configureAndroidCompose(this)
            }
        }
    }
}
