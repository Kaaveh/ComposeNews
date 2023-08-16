plugins {
    id("composenews.android.feature.presentation")
}

android {
    namespace = "ir.composenews.marketlist"
}

dependencies {
    implementation(project(":data:market-repository"))
}