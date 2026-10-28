plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ir.composenews.domain"
}

dependencies {
    // Koin
    implementation(libs.koin.core)

    projects.apply {
        api(core.network.ktor)
        testImplementation(core.testing)
    }
    libs.apply {
        implementation(lifecycle.viewmodel)
        api(kotlinx.collections.immutable)
        api(kotlinx.serialization.json)
        api(paging.common)
        testImplementation(coroutines.test)
        testImplementation(mockk)
    }
}
