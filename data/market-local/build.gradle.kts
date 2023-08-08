plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    id("composenews.android.room")
}

android {
    namespace = "ir.kaaveh.localdatasource"
}

dependencies {
    libs.apply {
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        androidTestImplementation(coroutines.test)
    }
}