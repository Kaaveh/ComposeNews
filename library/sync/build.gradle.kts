plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
}

android {
    namespace = "ir.kaaveh.sync"
}

dependencies {
    api(project(":domain:market"))
    libs.apply {
        implementation(hilt.work)
        implementation(startup.runtime)
        implementation(work.runtime.ktx)
    }
}