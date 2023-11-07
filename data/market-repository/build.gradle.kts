plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
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