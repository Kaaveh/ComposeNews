plugins {
    alias(libs.plugins.composenews.android.application)
}

android {
    namespace = libs.versions.projectApplicationId.get()

    defaultConfig {
        applicationId = libs.versions.projectApplicationId.get()
        minSdk = libs.versions.projectMinSdkVersionWear.get().toInt()
    }
}

dependencies {
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    projects.apply {
        implementation(appWatch.navigation)
        implementation(library.designsystem)
        implementation(core.base)
        implementation(core.uimarket)
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
        implementation(work.runtime.ktx)
        implementation(compose.activity)
        implementation(compose.ui.preview.wear)
        implementation(compose.foundation.wear)
        implementation(compose.horologist.layout)
        androidTestImplementation(platform(compose.bom))
        androidTestImplementation(compose.ui.test.junit4)
        debugImplementation(compose.ui.tooling)
        debugImplementation(compose.ui.test.manifest)
    }
}
