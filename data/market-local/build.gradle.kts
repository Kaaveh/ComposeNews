plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "ir.composenews.localdatasource"
}


dependencies {
    api(projects.data.sqldelight)
    libs.apply {
        implementation(bundles.koin)
        testImplementation(runner)
    }
    projects.apply {
        testImplementation(core.test)
    }
}
