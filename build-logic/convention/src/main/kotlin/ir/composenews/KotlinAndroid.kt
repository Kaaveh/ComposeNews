package ir.composenews

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *,*>,
) {
    commonExtension.apply {
        compileSdk = Integer.parseInt(libs.findVersion("projectCompileSdkVersion").get().toString())
        defaultConfig {
            minSdk = Integer.parseInt(libs.findVersion("projectMinSdkVersion").get().toString())
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions {
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlin.Experimental",
            )

            // Set JVM target to 17
            jvmTarget = JavaVersion.VERSION_17.toString()
        }

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
            unitTests.all {
                it.useJUnitPlatform()
            }
        }

        packaging {
            resources {
                resources.excludes.add("META-INF/*")
                resources.merges.add("META-INF/*.version")

                // https://github.com/Kotlin/kotlinx.coroutines/issues/2023#issuecomment-858644393
                resources.pickFirsts.add("win32-x86-64/attach_hotspot_windows.dll")
                resources.pickFirsts.add("win32-x86/attach_hotspot_windows.dll")
                resources.excludes.add("META-INF/licenses/ASM")
            }
        }
    }
}
