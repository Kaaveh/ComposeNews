plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.app_watch.ui"

    defaultConfig {
        minSdk = libs.versions.projectMinSdkVersionWear.get().toInt()
    }
}

dependencies {
    // Koin
    implementation(libs.koin.androidx.compose)

    api(projects.library.designsystem)
    api(projects.core.base)

    projects.apply {
        implementation(feature.marketlist.presentation)
        implementation(feature.marketdetail.presentation)
        implementation(core.ui)
        implementation(core.uimarket)
    }
    libs.apply {
        implementation(compose.ui.preview.wear)
        implementation(compose.foundation.wear)
        implementation(compose.material3.wear)
    }
}
