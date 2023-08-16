plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    id("composenews.android.room")
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
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        androidTestImplementation(coroutines.test)
        androidTestImplementation(runner)
    }
}