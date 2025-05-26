plugins {
    alias(libs.plugins.composenews.android.application)
    alias(libs.plugins.composenews.android.application.compose)
    alias(libs.plugins.composenews.android.hilt)
}

android {
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
        implementation(library.designsystem)
        implementation(core.base)
        implementation(core.uimarket)
    }
    libs.apply {
        implementation(androidx.ktx)
        implementation(hilt.work)
        implementation(lifecycle.runtime.ktx)
        implementation(hilt.navigation.compose)
        implementation(work.runtime.ktx)
        implementation(compose.activity)
        implementation(compose.ui.preview.wear)
        implementation(compose.foundation.wear)
        implementation(compose.horologist.layout)
        androidTestImplementation(platform(compose.bom))
        androidTestImplementation(compose.ui.test.junit4)
        debugImplementation(compose.ui.tooling)
        debugImplementation(compose.ui.test.manifest)
    }
}
