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
    projects.apply {
        testImplementation(core.test)
    }
}