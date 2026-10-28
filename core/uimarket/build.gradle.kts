plugins {
    alias(libs.plugins.composenews.android.library.compose)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ir.composenews.uimarket"
}

dependencies {
    projects.apply {
        implementation(domain.market)
    }
    libs.apply {
        api(platform(compose.bom))
        api(bundles.compose)
        api(kotlinx.serialization.json)
        testImplementation(bundles.kotest)
    }
}
