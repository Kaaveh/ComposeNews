plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.composenews.navigation"
}

dependencies {
    projects.apply {
        api(feature.marketlist)
        api(feature.marketdetail)
        implementation(core.uimarket)
        implementation(domain.market)
    }
    api(libs.navigation.compose)
    implementation(libs.compose.material3.adaptive.navigation)
    implementation(libs.compose.material3.adaptive.navigation.suite)
}