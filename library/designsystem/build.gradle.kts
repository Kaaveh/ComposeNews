plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.kaaveh.designsystem"
}

dependencies {
    libs.apply {
        api(platform(compose.bom))
        api(compose.ui)
        api(compose.ui.preview)
        api(compose.ui.tooling)
        api(compose.ui.test.manifest)
        api(compose.material)
        api(compose.coil)
        api(lifecycle.runtime.compose)
    }
}