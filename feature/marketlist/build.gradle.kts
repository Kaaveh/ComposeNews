plugins {
    id("composenews.android.feature.presentation")
}

android {
    namespace = "ir.composenews.marketlist"
}

dependencies {
    implementation(projects.data.marketRepository)
    implementation(libs.kotlinx.collections.immutable)
}