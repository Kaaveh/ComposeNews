plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.localdatasource"
}

dependencies {
    // Koin
    implementation(libs.koin.android)

    api(projects.data.sqldelight)
    libs.apply {
        testImplementation(runner)
        testImplementation(sqldelight.test)
    }
    projects.apply {
        testImplementation(core.testing)
    }
}
