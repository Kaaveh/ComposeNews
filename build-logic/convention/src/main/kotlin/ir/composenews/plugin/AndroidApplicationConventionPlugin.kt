package ir.composenews.plugin

import ir.composenews.config.Config
import ir.composenews.utils.applicationExtension
import ir.composenews.conv.configureKotlinAndroid
import ir.composenews.utils.applyPlugins
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyPlugins {
                listOf(
                    vLibs.plugin("android-application").pluginId,
                    vLibs.plugin("kotlin-android").pluginId,
                    vLibs.plugin("compose").pluginId,
                )
            }

            applicationExtension {
                namespace = Config.applicationId
                defaultConfig {
                    applicationId = Config.applicationId
                    versionCode = Config.versionCode
                    versionName = Config.versionName
                    minSdk = Config.minSdkVersionWear
                    targetSdk = Config.targetSdkVersion

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
