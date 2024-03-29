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
    libs.apply {
        implementation(retrofit)
        implementation(kotlinx.serialization.json)
        implementation(kotlinx.serialization.converter)
        implementation(logging.interceptor)
        implementation(lifecycle.viewmodel.ktx)
        debugImplementation(chucker)
        releaseImplementation(chucker.no.op)
        androidTestImplementation(runner)
    }
}