plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.sync"
}

dependencies {
    api(projects.domain.market)
    libs.apply {
        implementation(koin.work)
        implementation(startup.runtime)
        implementation(work.runtime.ktx)
    }
}
