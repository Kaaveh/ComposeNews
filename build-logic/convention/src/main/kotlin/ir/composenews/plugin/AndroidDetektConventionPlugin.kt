package ir.composenews.plugin

import ir.composenews.conv.configureDetekt
import ir.composenews.utils.applyPlugins
import ir.composenews.utils.detektExtension
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidDetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins {
                listOf(
                    vLibs.plugin("detekt").pluginId,
                )
            }

            detektExtension {
                configureDetekt(this)
            }
        }
    }
}
