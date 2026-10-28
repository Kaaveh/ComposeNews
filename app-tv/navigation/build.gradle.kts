plugins {
    alias(libs.plugins.composenews.android.library.compose)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ir.composenews.app_tv.navigation"
}

dependencies {
    projects.apply {
        implementation(appTv.ui)
        implementation(core.base)
        implementation(core.uimarket)
    }
    libs.apply {
        api(navigation3.runtime)
        api(navigation3.ui)
        implementation(lifecycle.viewmodel.navigation3)
        implementation(kotlinx.serialization.json)
    }
}
