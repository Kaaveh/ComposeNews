plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.navigation"
}

dependencies {
    projects.apply {
        implementation(feature.marketlist)
        implementation(feature.marketdetail)
        implementation(core.uimarket)
        implementation(domain.market)
    }
    api(libs.navigation.compose)
    implementation(libs.compose.material3.adaptive.navigation)
    implementation(libs.compose.material3.adaptive.navigation.suite)
}
