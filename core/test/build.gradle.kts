plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.hilt)
}

android {
    namespace = "ir.composenews.core_test"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    libs.apply {
        api(bundles.kotest)
        api(coroutines.test)
        api(mockk)
    }
}
