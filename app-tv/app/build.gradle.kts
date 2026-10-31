plugins {
    alias(libs.plugins.composenews.android.application)
}

android {
    namespace = "ir.composenews.tv"

    defaultConfig {
        applicationId = "${libs.versions.projectApplicationId.get()}.tv"
    }
}

dependencies {
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    projects.apply {
        implementation(appTv.ui)
        implementation(library.designsystem)
        implementation(core.base)
        implementation(domain.market)
        implementation(data.marketLocal)
        implementation(data.marketRemote)
        implementation(data.marketRepository)
        implementation(feature.marketlist.presentation)
        implementation(feature.marketdetail.presentation)
    }
    libs.apply {
        implementation(ktor.client.okhttp)
        implementation(androidx.ktx)
        implementation(lifecycle.runtime)
        implementation(compose.activity)
        implementation(compose.tv.material)
        androidTestImplementation(platform(compose.bom))
        androidTestImplementation(compose.ui.test.junit4)
        debugImplementation(compose.ui.tooling)
        debugImplementation(compose.ui.test.manifest)
    }
}
