package ir.composenews.plugin

import ir.composenews.utils.applyPlugins
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKotlinterConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins {
                listOf(
                    vLibs.plugin("kotliner").pluginId,
                )
            }
        }
    }
}
