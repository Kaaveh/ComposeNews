plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.kaaveh.navigation"
}

dependencies {
    projects.apply {
        implementation(feature.marketlist)
        implementation(feature.marketdetail)
        implementation(domain.market)
    }
    api(libs.navigation.compose)
}