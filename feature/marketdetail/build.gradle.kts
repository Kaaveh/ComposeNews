plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.newsdetail"
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
    implementation(libs.koin.core.viewmodel)

    // Feature dependencies
    androidTestImplementation(libs.runner)
    testImplementation(testFixtures(projects.core.test))
    androidTestImplementation(testFixtures(projects.core.test))
    api(projects.library.designsystem)
    api(projects.core.base)

    projects.apply {
        implementation(data.marketRepository)
        implementation(core.uimarket)
    }
    libs.apply {
        testImplementation(turbine)
        testImplementation(robolectric)
        testImplementation(compose.ui.test.junit4)
    }
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(platform(libs.compose.bom))
}
