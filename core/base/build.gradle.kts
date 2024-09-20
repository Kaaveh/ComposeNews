plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.base"
}

dependencies {
    projects.library.apply {
        api(projects.core.test)
        api(projects.core.network.ktor)
        implementation(projects.library.designsystem)
    }
    libs.apply {
        implementation(lifecycle.viewmodel.ktx)
    }
}