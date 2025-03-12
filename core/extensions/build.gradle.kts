plugins {
    alias(libs.plugins.composenews.android.library)
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
