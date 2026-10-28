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

/**
 * Shared (KMP) library with an Android target. Each module sets `kotlin { android { namespace = ... } }`.
 *
 * iOS targets are off until #368; turn them on with `composenews.ios=true` in `gradle.properties`.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        applyPlugin("kotlin-multiplatform")
        applyPlugin("android-kotlin-multiplatform-library")
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

            if (iosEnabled) {
                iosArm64()
                iosSimulatorArm64()
            }

            compilerOptions.configureKotlin()
            sourceSets.commonTest.dependencies { implementation(kotlin("test")) }
        }

        // The plain `detekt` task only looks in src/main and src/test, which a KMP module doesn't have.
        tasks.named("detekt", SourceTask::class.java) { setSource(file("src")) }

        // KMP has no `test` task, so CI's `./gradlew test` would skip the host tests.
        tasks.register("test") { dependsOn("testAndroid") }
        tasks.withType<Test>().configureEach { useJUnitPlatform() }
    }
}

internal val Project.iosEnabled: Boolean
    get() = providers.gradleProperty("composenews.ios").orNull.toBoolean()
