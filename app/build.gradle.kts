@file:Suppress("ForbiddenComment")
plugins {
    alias(libs.plugins.composenews.android.application)
    alias(libs.plugins.composenews.android.application.compose)
    alias(libs.plugins.composenews.android.hilt)}

android {
    namespace = libs.versions.projectApplicationId.get()
    defaultConfig {
        applicationId = libs.versions.projectApplicationId.get()
        versionCode = libs.versions.projectVersionCode.get().toInt()
        versionName = libs.versions.projectVersionName.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
        }
    }
}

dependencies {
    projects.apply {
        implementation(library.navigation)
        implementation(library.designsystem)
        implementation(core.sync)
        implementation(core.base)
        implementation(core.uimarket)
    }
    libs.apply {
        implementation(compose.activity)
        implementation(androidx.ktx)
        implementation(hilt.work)
        implementation(lifecycle.runtime.ktx)
        implementation(work.runtime.ktx)
        implementation(hilt.navigation.compose)
        implementation(compose.material3.adaptive.navigation.suite)
        implementation(compose.material3.adaptive.navigation)
        implementation(espresso.core)
        implementation(compose.ui.test.manifest)
        implementation(compose.ui.test.junit4)
    }
}
