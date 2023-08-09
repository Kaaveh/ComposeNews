plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    id("composenews.android.room")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "ir.kaaveh.data"
}

dependencies {
    api(project(":domain:market"))
    implementation(project(":data:market-remote"))
    implementation(project(":data:market-local"))
    libs.apply {
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        androidTestImplementation(coroutines.test)
        androidTestImplementation(runner)
    }
}