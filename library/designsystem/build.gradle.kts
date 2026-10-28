plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.designsystem"
}

dependencies {
    libs.apply {
        api(platform(compose.bom))
        api(bundles.compose)
        api(compose.coil)
        api(lifecycle.runtime.compose)
        api(libs.compottie)
        implementation(androidx.ktx)
        implementation(compose.icons.core)
        implementation(compose.icons.extended)
    }
}
