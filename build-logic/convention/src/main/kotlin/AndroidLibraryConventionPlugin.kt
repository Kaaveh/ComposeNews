import ir.composenews.libraryExtension
import ir.composenews.configureKotlinAndroid
import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins()
            applyDependencies()
            libraryExtension {
                configureKotlinAndroid(this)
            }
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply(libs.findPlugin("composenews-android-detekt").get().get().pluginId)
            apply(libs.findPlugin("composenews-android-ktlint").get().get().pluginId)
        }
    }

    private fun Project.applyDependencies() {
        dependencies {
            "androidTestImplementation"(kotlin("test"))
            "testImplementation"(kotlin("test"))
        }
    }
}
