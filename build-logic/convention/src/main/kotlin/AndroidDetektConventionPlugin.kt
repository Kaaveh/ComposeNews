import ir.composenews.configureDetekt
import ir.composenews.detektGradle
import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidDetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins()
            detektGradle {
                configureDetekt(this)
            }
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply(libs.findLibrary("detekt-gradlePlugin").get().get().group.toString())
        }
    }
}
