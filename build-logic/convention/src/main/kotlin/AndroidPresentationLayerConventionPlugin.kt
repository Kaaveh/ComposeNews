import com.android.build.gradle.LibraryExtension
import ir.kaaveh.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidPresentationLayerConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("composenews.android.hilt")
                apply("composenews.android.library")
                apply("composenews.android.library.compose")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt.android").get())
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())
                add("kapt", libs.findLibrary("dager.hilt.compiler").get())


                add("testImplementation", project(":library:core-test"))
                add("api", project(":library:designsystem"))
                add("api", project(":library:base"))
            }
        }
    }
}