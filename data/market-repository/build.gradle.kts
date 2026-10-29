plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.data"
}

dependencies {
    // Logging
    implementation(libs.kermit)

    // Koin
    implementation(libs.koin.core)

    projects.apply {
        api(domain.market)
        implementation(data.marketRemote)
        implementation(data.marketLocal)
        testImplementation(core.testing)
    }
    libs.apply {
        testImplementation(mockk)
    }
}
