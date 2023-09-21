import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jmailen.gradle.kotlinter.KotlinterExtension

class AndroidKotlinterConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager) {
                apply("org.jmailen.kotlinter")
            }
            val extension = extensions.getByType<KotlinterExtension>()
           with(extension) {

           }
        }
    }
}