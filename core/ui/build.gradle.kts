plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.core.ui"
}

dependencies {
    projects.apply {
        api(core.base)
        api(core.uimarket)
        api(domain.market)
    }
    libs.apply {
        implementation(compose.ui)
        implementation(lifecycle.runtime.compose)
    }
}
