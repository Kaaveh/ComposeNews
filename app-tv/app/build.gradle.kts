plugins {
    alias(libs.plugins.composenews.android.application)
    alias(libs.plugins.kotlinx.serialization)
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
    implementation(libs.koin.compose.viewmodel)

    projects.apply {
        implementation(appTv.navigation)
        implementation(appTv.ui)
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
        implementation(lifecycle.runtime.compose)
        implementation(compose.activity)
        implementation(compose.tv.material)
        implementation(navigation3.runtime)
        implementation(navigation3.ui)
        implementation(lifecycle.viewmodel.navigation3)
        androidTestImplementation(platform(compose.bom))
        androidTestImplementation(compose.ui.test.junit4)
        debugImplementation(compose.ui.tooling)
        debugImplementation(compose.ui.test.manifest)
    }
}
