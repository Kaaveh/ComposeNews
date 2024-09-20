import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKotlinterConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins()
            tasks.withType()
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("org.jmailen.kotlinter")
        }
    }
}
