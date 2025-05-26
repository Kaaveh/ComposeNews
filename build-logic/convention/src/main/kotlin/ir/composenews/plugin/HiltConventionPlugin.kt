package ir.composenews.plugin

import ir.composenews.utils.applyPlugins
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins {
                listOf(
                    vLibs.plugin("hilt-android").pluginId,
                    vLibs.plugin("ksp").pluginId,
                )
            }

            dependencies {
                "implementation"(vLibs.findLibrary("hilt.android").get())
                "ksp"(vLibs.findLibrary("hilt.compiler").get())
            }
        }
    }
}
