plugins {
    id("composenews.android.feature.presentation")
}

android {
    namespace = "ir.kaaveh.marketlist"
}

dependencies {
    implementation(project(":data:market-repository"))
}