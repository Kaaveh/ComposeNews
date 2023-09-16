import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import ir.composenews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

class AndroidDetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.findLibrary("detekt-gradlePlugin").get().get().group)
            val extension = extensions.getByType<DetektExtension>()
            extension.apply {
                tasks.named<Detekt>("detekt") {
                    reports {
                        xml.required.set(true)
                        html.required.set(true)
                        txt.required.set(true)
                        sarif.required.set(true)
                        md.required.set(true)
                    }
                }
            }
            dependencies {
                "detektPlugins"(libs.findLibrary("detekt-formatting").get())
            }
        }
    }
}