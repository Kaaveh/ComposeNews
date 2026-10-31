plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.app_watch.navigation"

    defaultConfig {
        minSdk = libs.versions.projectMinSdkVersionWear.get().toInt()
    }
}

dependencies {
    projects.apply {
        implementation(appWatch.ui)
        implementation(core.uimarket)
    }
    implementation(libs.navigation.compose.wear)
}
