package ir.composenews.buildlogic

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest

/**
 * Shared (KMP) library with Android and iOS targets. Each module sets `kotlin { android { namespace = ... } }`.
 *
 * Kotest on iOS needs the `io.kotest` Gradle plugin, which runs its KSP processor on the iOS test
 * compilations to register the specs. KSP has to be applied first. Modules without `src/commonTest` skip both.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        applyPlugin("kotlin-multiplatform")
        applyPlugin("android-kotlin-multiplatform-library")
        if (file("src/commonTest").exists()) {
            applyPlugin("ksp")
            applyPlugin("kotest")
        }
        applyCodeQualityPlugins()

        extensions.configure<KotlinMultiplatformExtension> {
            targets.withType<KotlinMultiplatformAndroidLibraryTarget>().configureEach {
                compileSdk = libs.version("projectCompileSdkVersion").toInt()
                minSdk = libs.version("projectMinSdkVersion").toInt()
                withHostTest {
                    isIncludeAndroidResources = true
                    isReturnDefaultValues = true
                }
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }

            iosTargets()

            compilerOptions.configureKotlin()
            sourceSets.commonTest.dependencies {
                implementation(kotlin("test"))
                implementation(libs.library("kotest-frameworkEngine"))
                implementation(libs.library("kotest-assertionsCore"))
            }
            // The JUnit Platform runner is JVM-only, so it goes on the Android host tests.
            sourceSets.matching { it.name == "androidHostTest" }.configureEach {
                dependencies { implementation(libs.library("kotest-runnerJUnit5")) }
            }
        }

        detektKmpSources()

        // KSP (Kotest's spec registry, Room's DAOs) adds its output to the iOS source sets, so Kotlinter would
        // lint generated code.
        val buildDir = layout.buildDirectory.get().asFile
        tasks
            .withType<SourceTask>()
            .matching { it.name.startsWith("lintKotlin") || it.name.startsWith("formatKotlin") }
            .configureEach { exclude { it.file.startsWith(buildDir) } }

        // KMP has no `test` task, so CI's `./gradlew test` would skip the host tests.
        tasks.register("test") { dependsOn("testAndroid") }
        tasks.withType<Test>().configureEach { useJUnitPlatform() }

        // CI passes the simulator it picked (`-PiosSimulatorDevice=<udid>`); otherwise KGP uses its default.
        val simulator = providers.gradleProperty("iosSimulatorDevice")
        tasks.withType<KotlinNativeSimulatorTest>().configureEach { if (simulator.isPresent) device.set(simulator) }
    }
}
