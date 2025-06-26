package ir.composenews.plugin

import ir.composenews.utils.applicationExtension
import ir.composenews.conv.configureKotlinAndroid
import ir.composenews.utils.applyPlugins
import ir.composenews.utils.plugin
import ir.composenews.utils.vLibs
import ir.composenews.utils.version
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
                namespace = vLibs.version("projectApplicationId")
                defaultConfig {
                    applicationId = vLibs.version("projectApplicationId")
                    versionCode = Integer.parseInt(vLibs.version("projectVersionCode"))
                    versionName = vLibs.version("projectVersionName")
                    minSdk = Integer.parseInt(vLibs.version("projectMinSdkVersion"))
                    targetSdk = Integer.parseInt(vLibs.version("projectTargetSdkVersion"))

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
