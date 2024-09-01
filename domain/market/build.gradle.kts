plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    libs.plugins.apply {
        alias(kotlin.parcelize)
    }
}

android {
    namespace = "ir.composenews.domain"

}

dependencies {
    api(projects.core.test)
    api(projects.core.network.ktor)
    libs.apply {
        implementation(javax.inject)
        implementation(coroutines)
        implementation(lifecycle.viewmodel.ktx)
        api(kotlinx.collections.immutable)
        testImplementation(coroutines.test)
        testImplementation(mokito.kotlin)
    }
}