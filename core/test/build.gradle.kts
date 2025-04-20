plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.core_test"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    libs.apply {
        api(bundles.koin)
        api(bundles.kotest)
        api(coroutines.test)
        api(mockk)
    }
}
