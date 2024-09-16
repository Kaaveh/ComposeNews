package ir.composenews

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *,*>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
        }

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
        }

    }
}