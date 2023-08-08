plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
    id("composenews.android.hilt")
    id("composenews.android.feature.presentation")
}

android {
    namespace = "ir.kaaveh.marketlist"
}

dependencies {
    implementation(project(":data:market-repository"))
}