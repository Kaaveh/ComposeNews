plugins {
    alias(libs.plugins.composenews.android.library.compose)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ir.composenews.navigation"
}

dependencies {
    projects.apply {
        implementation(feature.marketlist.ui)
        implementation(feature.marketdetail.ui)
        implementation(core.ui)
        implementation(core.uimarket)
        implementation(domain.market)
    }
    libs.apply {
        api(navigation3.runtime)
        api(navigation3.ui)
        implementation(lifecycle.viewmodel.navigation3)
        implementation(kotlinx.serialization.json)
        implementation(koin.compose.viewmodel)
        implementation(compose.material3.adaptive.navigation)
        implementation(compose.material3.adaptive.navigation.suite)
    }
}
