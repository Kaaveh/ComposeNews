import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyDependencies()
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
//            apply("org.jetbrains.kotlin.kapt")
        }
    }

    private fun Project.applyDependencies() {
        dependencies {
            "implementation"(libs.findBundle("koin").get())
        }
    }
}