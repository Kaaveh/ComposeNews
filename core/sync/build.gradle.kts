plugins {
    id("composenews.android.library")
    id("composenews.android.koin")
}

android {
    namespace = "ir.composenews.sync"
}

dependencies {
    api(projects.domain.market)
    libs.apply {
        implementation(startup.runtime)
        implementation(work.runtime.ktx)
    }
}