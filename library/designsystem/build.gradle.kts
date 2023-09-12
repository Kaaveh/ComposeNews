plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
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
        api(window.size)
        api(accomponist.adaptive)
    }
}