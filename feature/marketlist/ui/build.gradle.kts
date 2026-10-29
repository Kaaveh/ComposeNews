plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.marketlist.ui"
}

configurations.all {
    resolutionStrategy {
        force("androidx.test:runner:1.4.0")
    }
}

dependencies {
    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.compose.activity)
    implementation(libs.koin.compose.viewmodel)

    // Feature dependencies
    androidTestImplementation(libs.runner)
    testImplementation(projects.core.testing)
    api(projects.feature.marketlist.presentation)
    api(projects.library.designsystem)

    projects.apply {
        implementation(core.ui)
        implementation(core.uimarket)
        implementation(core.extensions)
    }
    libs.apply {
        implementation(paging.compose)
        testImplementation(robolectric)
        testImplementation(compose.ui.test.junit4)
    }
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(platform(libs.compose.bom))
}
