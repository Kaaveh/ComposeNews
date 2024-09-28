plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    libs.plugins.apply {
        alias(kotlinx.serialization)
    }
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