plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
}

android {
    namespace = "ir.kaaveh.localdatasource"
}

dependencies {
    libs.apply {
        implementation(room.runtime)
        implementation(room.ktx)
        kapt(room.compiler)
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        androidTestImplementation(coroutines.test)
    }
}