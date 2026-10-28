plugins {
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.marketlist"
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
    implementation(libs.koin.core.viewmodel)

    // Feature dependencies
    androidTestImplementation(libs.runner)
    testImplementation(projects.core.testing)
    testImplementation(libs.mockk)
    api(projects.library.designsystem)
    api(projects.core.base)

    projects.apply {
        implementation(data.marketRepository)
        implementation(core.uimarket)
        implementation(core.extensions)
    }
    libs.apply {
        implementation(kotlinx.collections.immutable)
        implementation(paging.compose)
        testImplementation(turbine)
        testImplementation(robolectric)
        testImplementation(compose.ui.test.junit4)
    }
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(platform(libs.compose.bom))
}
