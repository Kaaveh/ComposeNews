plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.base"
}

dependencies {
    projects.apply {
        api(core.network.ktor)
        implementation(library.designsystem)
    }
    libs.apply {
        implementation(koin.core)
        implementation(lifecycle.viewmodel)
    }
}
