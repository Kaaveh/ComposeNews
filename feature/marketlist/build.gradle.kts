plugins {
    id("composenews.android.feature.presentation")
}

android {
    namespace = "ir.kaaveh.marketlist"
}

dependencies {
    implementation(projects.data.marketRepository)
}