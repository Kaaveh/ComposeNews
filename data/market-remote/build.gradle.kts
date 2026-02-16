import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotliner)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ir.composenews.remotedatasource"
    compileSdk = libs.versions.projectCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.projectMinSdkVersion.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
            excludes += "META-INF/*"
            merges += "META-INF/*.version"
            pickFirsts += "win32-x86-64/attach_hotspot_windows.dll"
            pickFirsts += "win32-x86/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/ASM"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

dependencies {
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Test
    androidTestImplementation(kotlin("test"))
    testImplementation(kotlin("test"))

    api(projects.core.network.ktor)
    libs.apply {
        implementation(bundles.ktor)
        debugImplementation(chucker)
        releaseImplementation(chucker.no.op)
        androidTestImplementation(runner)
    }
}
