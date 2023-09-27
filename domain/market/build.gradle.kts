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
    libs.apply {
        implementation(javax.inject)
        implementation(coroutines)
        implementation(lifecycle.viewmodel.ktx)
        api(kotlinx.collections.immutable)
        testImplementation(junit)
        testImplementation(coroutines.test)
        testImplementation(mokito.kotlin)
    }
}