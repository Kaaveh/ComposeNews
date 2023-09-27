plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.composenews.uimarket"
}

dependencies {
    implementation(projects.domain.market)
}