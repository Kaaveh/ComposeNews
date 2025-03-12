plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "ir.composenews.data"
}

dependencies {
    projects.apply {
        api(domain.market)
        implementation(data.marketRemote)
        implementation(data.marketLocal)
    }
    libs.apply {
        testImplementation(bundles.kotest)
        testImplementation(sqldelight.test)
    }
}
