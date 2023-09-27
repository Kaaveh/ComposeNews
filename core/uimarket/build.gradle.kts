plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.composenews.uimarket"
}

dependencies {
    implementation(projects.domain.market)
    libs.apply {
        api(platform(compose.bom))
        api(bundles.compose)
    }
}