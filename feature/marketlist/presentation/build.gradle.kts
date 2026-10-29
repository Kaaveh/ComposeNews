plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.marketlist"
}

dependencies {
    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.core.viewmodel)

    api(projects.core.base)

    projects.apply {
        implementation(domain.market)
        implementation(core.uimarket)
    }
    testImplementation(projects.core.testing)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
}
