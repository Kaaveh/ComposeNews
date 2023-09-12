plugins {
    id("composenews.android.feature.presentation")
}

android {
    namespace = "ir.composenews.newsdetail"
}

dependencies {
    implementation(projects.data.marketRepository)
}