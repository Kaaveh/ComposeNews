@file:Suppress("ktlint")

package ir.composenews.utils

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

inline fun Project.libraryExtension(
    crossinline configure: LibraryExtension.() -> Unit
) = extensions.configure<LibraryExtension> { configure() }

inline fun Project.detektExtension(
    crossinline configure: DetektExtension.() -> Unit
) = extensions.configure<DetektExtension> { configure() }

inline fun Project.applicationExtension(
    crossinline configure: ApplicationExtension.() -> Unit
) = extensions.configure<ApplicationExtension> { configure() }

inline fun Project.applyPlugins(crossinline plugin: () -> List<String>) {
    pluginManager.apply {
        plugin().forEach {
            apply(plugin = it)
        }
    }
}

fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
