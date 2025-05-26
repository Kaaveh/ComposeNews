package ir.composenews.conv

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import ir.composenews.utils.custom
import ir.composenews.utils.library
import ir.composenews.utils.vLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

internal fun Project.configureDetekt(extension: DetektExtension) = extension.apply {
    tasks.named<Detekt>("detekt") {
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
    }
    dependencies {
        custom("detektPlugins", vLibs.library("detekt-formatting"))
    }
}
