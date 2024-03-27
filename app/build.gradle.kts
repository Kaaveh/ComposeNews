plugins {
    id("composenews.android.application")
    id("composenews.android.application.compose")
    id("composenews.android.hilt")
}

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
        }
    }
}

dependencies {
    projects.library.apply {
        implementation(navigation)
        implementation(designsystem)
    }
    projects.core.apply {
        implementation(sync)
        implementation(base)
        implementation(uimarket)
    }
    libs.apply {
        implementation(compose.activity)
        implementation(androidx.ktx)
        implementation(hilt.work)
        implementation(lifecycle.runtime.ktx)
        implementation(work.runtime.ktx)
        implementation(libs.hilt.navigation.compose)
        implementation(libs.espresso.core)
        implementation(libs.compose.ui.test.manifest)
        implementation(libs.compose.ui.test.junit4)
    }
}
