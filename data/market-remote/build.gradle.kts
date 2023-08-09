plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "ir.kaaveh.remotedatasource"
}

dependencies {
    libs.apply {
        implementation(retrofit)
        implementation(converter.gson)
        implementation(logging.interceptor)
        debugImplementation(chucker)
        releaseImplementation(chucker.no.op)
        androidTestImplementation(runner)
    }
}