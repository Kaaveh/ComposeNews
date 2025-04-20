plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ir.composenews.domain"

}

dependencies {
    projects.apply {
        api(core.test)
        api(core.network.ktor)
    }
    libs.apply {
        implementation(coroutines)
        implementation(lifecycle.viewmodel.ktx)
        api(kotlinx.collections.immutable)
        testImplementation(coroutines.test)
        testImplementation(mokito.kotlin)
    }
}
