package ir.composenews.buildlogic

import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

/** Room on a [KmpLibraryConventionPlugin] module: KSP runs `room-compiler` once per target. */
class KmpRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(KmpLibraryConventionPlugin::class.java)
        applyPlugin("ksp")
        applyPlugin("room")

        extensions.configure<RoomExtension> { schemaDirectory("$projectDir/schemas") }
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies { implementation(libs.library("room-runtime")) }

            // The Android target only exists once the module script runs `kotlin { android { } }`.
            val compiler = libs.library("room-compiler")
            targets.configureEach {
                if (platformType != KotlinPlatformType.common) {
                    project.dependencies.add("ksp${name.replaceFirstChar(Char::uppercase)}", compiler)
                }
            }
        }
    }
}
