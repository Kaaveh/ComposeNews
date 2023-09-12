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
    @Suppress("UnstableApiUsage")
    buildTypes {
        val release by getting {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
    }
    libs.apply {
        implementation(compose.activity)
        implementation(androidx.ktx)
        implementation(hilt.work)
        implementation(lifecycle.runtime.ktx)
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        implementation(work.runtime.ktx)
        implementation(libs.hilt.navigation.compose)
    }
}
