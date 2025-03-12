@file:Suppress("ktlint")

package ir.composenews

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

inline fun Project.libraryExtension(
    crossinline configure: LibraryExtension.() -> Unit
) = extensions.configure<LibraryExtension> { configure() }

inline fun Project.detektExtension(
    crossinline configure: DetektExtension.() -> Unit
) = extensions.configure<DetektExtension> { configure() }

inline fun Project.applicationExtension(
    crossinline configure: ApplicationExtension.() -> Unit
) = extensions.configure<ApplicationExtension> { configure() }

fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
