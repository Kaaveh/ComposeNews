plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.composenews.app_watch.designsystem"
}

dependencies {
    implementation(libs.compose.material.wear)
}