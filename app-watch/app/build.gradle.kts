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
        minSdk = libs.versions.projectMinSdkVersionWear.get().toInt()
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

    projects.apply {
        implementation(appWatch.navigation)
        implementation(appWatch.designsystem)
    }

    projects.core.apply {
        implementation(base)
        implementation(uimarket)
    }

    libs.apply {
        implementation(compose.activity)
        implementation(androidx.ktx)
        implementation(hilt.work)
        implementation(lifecycle.runtime.ktx)
        implementation(libs.hilt.navigation.compose)
        implementation(libs.compose.ui.preview.wear)
        implementation(libs.compose.foundation.wear)
        implementation(libs.compose.horologist.layout)
        implementation(work.runtime.ktx)
        androidTestImplementation(platform(libs.compose.bom))
        androidTestImplementation(libs.compose.ui.test.junit4)
        debugImplementation(libs.compose.ui.tooling)
        debugImplementation(libs.compose.ui.test.manifest)
    }
}