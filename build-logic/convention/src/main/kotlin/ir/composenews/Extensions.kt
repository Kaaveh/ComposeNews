package ir.composenews

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

inline fun Project.androidGradle(crossinline configure: LibraryExtension.() -> Unit) =
    extensions.configure<LibraryExtension> {
        configure()
    }