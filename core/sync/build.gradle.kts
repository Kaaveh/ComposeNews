plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.sync"
}

dependencies {
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.workmanager)

    api(projects.domain.market)
    libs.apply {
        implementation(startup.runtime)
        implementation(work.runtime.ktx)
    }
}
