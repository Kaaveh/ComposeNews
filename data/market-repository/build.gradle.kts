plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
}

android {
    namespace = "ir.kaaveh.data"
}

dependencies {
    api(project(":domain:market"))
    implementation(project(":data:market-remote"))
    implementation(project(":data:market-local"))
    libs.apply {
        implementation(lifecycle.viewmodel.ktx)
        implementation(room.runtime)
        implementation(room.ktx)
        kapt(room.compiler)
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        androidTestImplementation(coroutines.test)
        androidTestImplementation(runner)
    }
}