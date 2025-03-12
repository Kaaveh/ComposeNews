plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "ir.composenews.remotedatasource"
}

dependencies {
    api(projects.core.network.ktor)
    libs.apply {
        implementation(bundles.ktor)
        debugImplementation(chucker)
        releaseImplementation(chucker.no.op)
        androidTestImplementation(runner)
    }
}
