import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotliner)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ir.composenews.navigation"
    compileSdk = libs.versions.projectCompileSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.projectMinSdkVersion.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
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
    // Compose BOM
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    // Test
    androidTestImplementation(kotlin("test"))
    testImplementation(kotlin("test"))

    projects.apply {
        implementation(feature.marketlist)
        implementation(feature.marketdetail)
        implementation(core.uimarket)
        implementation(domain.market)
    }
    libs.apply {
        api(navigation3.runtime)
        api(navigation3.ui)
        implementation(lifecycle.viewmodel.navigation3)
        implementation(kotlinx.serialization.json)
        implementation(compose.material3.adaptive.navigation)
        implementation(compose.material3.adaptive.navigation.suite)
    }
}
