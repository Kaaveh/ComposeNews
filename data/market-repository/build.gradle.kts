plugins {
    id("composenews.android.library")
    id("composenews.android.koin")
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
        api(data.marketRemote)
        api(data.marketLocal)
    }
    libs.apply {
        testImplementation(bundles.kotest)
        testImplementation(sqldelight.test)
    }
}