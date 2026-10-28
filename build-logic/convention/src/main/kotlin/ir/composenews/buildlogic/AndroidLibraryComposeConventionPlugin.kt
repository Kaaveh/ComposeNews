package ir.composenews.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** [AndroidLibraryConventionPlugin] plus Jetpack Compose. */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AndroidLibraryConventionPlugin::class.java)
        extensions.configure<LibraryExtension> { configureCompose(this) }
    }
}
