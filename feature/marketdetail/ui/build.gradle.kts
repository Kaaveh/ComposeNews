plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.marketdetail.ui"
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
    implementation(libs.koin.compose.viewmodel)

    // Feature dependencies
    androidTestImplementation(libs.runner)
    testImplementation(projects.core.testing)
    androidTestImplementation(projects.core.testing)
    api(projects.feature.marketdetail.presentation)
    api(projects.library.designsystem)

    projects.apply {
        implementation(core.ui)
        implementation(core.uimarket)
    }
    libs.apply {
        testImplementation(robolectric)
        testImplementation(compose.ui.test.junit4)
    }
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(platform(libs.compose.bom))
}
