import ir.composenews.androidGradle
import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()
            applyDependencies()
            androidGradle {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("composenews.android.library")
            apply("composenews.android.library.compose")
            apply("composenews.android.hilt")
        }
    }

    private fun Project.applyDependencies() {
        dependencies {
            "implementation"(libs.findLibrary("hilt.navigation.compose").get())
            "androidTestImplementation"(libs.findLibrary("runner").get())

            "testImplementation"(project(":core:test"))
            "api"(project(":library:designsystem"))
            "api"(project(":core:base"))
        }
    }
}