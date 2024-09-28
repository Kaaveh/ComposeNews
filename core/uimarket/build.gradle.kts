plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
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