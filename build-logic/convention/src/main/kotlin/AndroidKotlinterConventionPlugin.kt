import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKotlinterConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins()
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("org.jmailen.kotlinter")
        }
    }
}
