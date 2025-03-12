plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.library.compose)
}

android {
    namespace = "ir.composenews.base"
}

dependencies {
    projects.apply {
        api(core.test)
        api(core.network.ktor)
        implementation(library.designsystem)
    }
    implementation(libs.lifecycle.viewmodel.ktx)
}
