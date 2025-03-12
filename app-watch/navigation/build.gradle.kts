plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.app_watch.navigation"
}

dependencies {
    projects.apply {
        implementation(appWatch.ui)
        implementation(core.uimarket)
    }
    implementation(libs.navigation.compose.wear)
}
