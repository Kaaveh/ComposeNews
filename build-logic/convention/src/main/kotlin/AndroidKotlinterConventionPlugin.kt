import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKotlinterConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager) {
                apply("org.jmailen.kotlinter")
            }
        }
    }
}
