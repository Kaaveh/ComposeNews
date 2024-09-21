package ir.composenews

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
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
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    }
    dependencies {
        "detektPlugins"(libs.findLibrary("detekt-formatting").get())
    }
    setupDetekt()
}


private fun Project.setupDetekt(vararg buildVariants: String) {
    val detektAll = tasks.register("detektAll") {
        group = "verification"
        description = "Run detekt analysis in all the modules."

        dependsOn(
            if (buildVariants.isEmpty()) {
                listOf(
                    project.tasks.named("detektMain"),
                    project.tasks.named("detektTest"),
                )
            } else {
                buildVariants.flatMap { variant ->
                    listOf(
                        project.tasks.named("detekt$variant"),
                        project.tasks.named("detekt${variant}UnitTest"),
                    )
                }
            }
        )
    }
}