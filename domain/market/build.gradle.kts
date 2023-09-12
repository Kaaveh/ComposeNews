plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    libs.plugins.apply {
        alias(kotlin.parcelize)
    }
}

android {
    namespace = "ir.composenews.domain"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get().toString()
    }
}

dependencies {
    api(projects.core.test)
    libs.apply {
        implementation((platform(compose.bom)))
        implementation(compose.runtime)
        implementation(javax.inject)
        implementation(coroutines)
        implementation(lifecycle.viewmodel.ktx)
        testImplementation(junit)
        testImplementation(coroutines.test)
        testImplementation(mokito.kotlin)
    }
}