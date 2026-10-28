package ir.composenews.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/** The app shells (`app`, `app-watch:app`, `app-tv:app`). All of them use Compose. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        applyPlugin("android-application")
        applyPlugin("kotlin-android")
        applyCodeQualityPlugins()

        extensions.configure<ApplicationExtension> {
            configureAndroid(this)
            configureCompose(this)

            defaultConfig {
                targetSdk = libs.version("projectTargetSdkVersion").toInt()
                versionCode = libs.version("projectVersionCode").toInt()
                versionName = libs.version("projectVersionName")
                vectorDrawables.useSupportLibrary = true
            }

            buildTypes {
                getByName("debug") {
                    isMinifyEnabled = false
                    isShrinkResources = false
                    applicationIdSuffix = ".debug"
                    versionNameSuffix = "-debug"
                }
                getByName("release") {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro",
                    )
                }
            }
        }
        extensions.configure<KotlinAndroidProjectExtension> { compilerOptions.configureKotlinJvm() }
    }
}
