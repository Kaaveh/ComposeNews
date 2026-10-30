plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.app_watch.ui"
}

configurations.all {
    resolutionStrategy {
        force("androidx.test:runner:1.4.0")
    }
}

dependencies {
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Feature dependencies
    androidTestImplementation(libs.runner)
    api(projects.library.designsystem)
    api(projects.core.base)

    projects.apply {
        implementation(feature.marketlist.presentation)
        implementation(feature.marketdetail.ui)
        implementation(core.ui)
        implementation(core.uimarket)
        implementation(core.extensions)
        implementation(data.marketRepository)
    }
    libs.apply {
        implementation(compose.ui.preview.wear)
        implementation(compose.foundation.wear)
        implementation(compose.horologist.layout)
        implementation(compose.ui.test.manifest)
        implementation(compose.ui.test.junit4)
    }
}
