plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.uimarket"
}

dependencies {
    projects.apply {
        implementation(domain.market)
        testImplementation(core.test)
    }
    libs.apply {
        api(platform(compose.bom))
        api(bundles.compose)
    }
}
