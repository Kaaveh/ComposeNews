import ir.composenews.androidGradle
import ir.composenews.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins()
            applyDependencies()
            androidGradle {
                configureKotlinAndroid(this)
            }
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("composenews.android.detekt")
            apply("composenews.android.ktlint")
        }
    }

    private fun Project.applyDependencies() {
        dependencies {
            "androidTestImplementation"(kotlin("test"))
            "testImplementation"(kotlin("test"))
        }
    }
}