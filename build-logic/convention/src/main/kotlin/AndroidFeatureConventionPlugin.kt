import com.android.build.gradle.LibraryExtension
import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("composenews.android.library")
                apply("composenews.android.library.compose")
                apply("composenews.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())
                add("androidTestImplementation", libs.findLibrary("runner").get())

                add("testImplementation", project(":core:test"))
                add("api", project(":library:designsystem"))
                add("api", project(":core:base"))
            }
        }
    }
}