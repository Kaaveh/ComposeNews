import ir.composenews.applicationGradle
import ir.composenews.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins()
            applicationGradle {
                configureAndroidCompose(this)
            }
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("com.android.application")
        }
    }

}