plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.composenews.app_watch.navigation"
}

dependencies {
    projects.apply {
        implementation(appWatch.ui)
        implementation(core.uimarket)
        implementation(domain.market)

    }
    implementation(libs.navigation.compose.wear)
}