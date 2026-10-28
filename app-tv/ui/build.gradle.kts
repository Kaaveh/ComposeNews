plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.app_tv.ui"
}

dependencies {
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    api(projects.library.designsystem)
    api(projects.core.base)

    projects.apply {
        implementation(feature.marketlist)
        implementation(feature.marketdetail)
        implementation(core.uimarket)
        implementation(core.extensions)
        implementation(data.marketRepository)
    }
    libs.apply {
        implementation(compose.tv.material)
        implementation(paging.compose)
        implementation(lifecycle.runtime.compose)
    }
}
