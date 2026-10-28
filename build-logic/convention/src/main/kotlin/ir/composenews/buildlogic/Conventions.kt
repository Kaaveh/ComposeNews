package ir.composenews.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.version(alias: String): String = findVersion(alias).get().requiredVersion

internal fun VersionCatalog.library(alias: String) = findLibrary(alias).get()

internal fun Project.applyPlugin(alias: String) {
    pluginManager.apply(libs.findPlugin(alias).get().get().pluginId)
}

/** Detekt and Kotlinter, applied to every module. */
internal fun Project.applyCodeQualityPlugins() {
    applyPlugin("detekt")
    applyPlugin("kotliner")
}

/** Every Kotlin compilation, JVM or not. */
internal fun KotlinCommonCompilerOptions.configureKotlin() {
    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}

internal fun KotlinJvmCompilerOptions.configureKotlinJvm() {
    configureKotlin()
    jvmTarget.set(JvmTarget.JVM_17)
}

/** The block every `com.android.application` and `com.android.library` module used to repeat. */
internal fun Project.configureAndroid(android: CommonExtension) {
    android.apply {
        compileSdk = libs.version("projectCompileSdkVersion").toInt()
        defaultConfig.minSdk = libs.version("projectMinSdkVersion").toInt()
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17

        testOptions.unitTests.isIncludeAndroidResources = true
        testOptions.unitTests.isReturnDefaultValues = true
        testOptions.unitTests.all { it.useJUnitPlatform() }

        packaging.resources.apply {
            excludes += "META-INF/*"
            merges += "META-INF/*.version"
            // https://github.com/Kotlin/kotlinx.coroutines/issues/2023#issuecomment-858644393
            pickFirsts += "win32-x86-64/attach_hotspot_windows.dll"
            pickFirsts += "win32-x86/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/ASM"
        }
    }

    dependencies {
        "testImplementation"(kotlin("test"))
        "androidTestImplementation"(kotlin("test"))
    }
}

/** The Compose compiler, `buildFeatures.compose` and the Compose BOM. */
internal fun Project.configureCompose(android: CommonExtension) {
    applyPlugin("compose")
    android.buildFeatures.compose = true

    val bom = libs.library("compose-bom")
    dependencies {
        "implementation"(platform(bom))
        "androidTestImplementation"(platform(bom))
    }
}
