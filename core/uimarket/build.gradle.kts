plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.uimarket"
}

dependencies {
    implementation(projects.domain.market)
    testImplementation(projects.core.test)
    libs.apply {
        api(platform(compose.bom))
        api(bundles.compose)
    }
}