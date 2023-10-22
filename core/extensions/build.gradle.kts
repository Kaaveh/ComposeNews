plugins {
    id("composenews.android.library")
}

android {
    namespace = "ir.composenews.extensions"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    libs.apply {
        implementation(junit)
    }
}