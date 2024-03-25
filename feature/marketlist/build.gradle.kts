plugins {
    id("composenews.android.feature")
}

android {
    namespace = "ir.composenews.marketlist"
}

configurations.all {
    resolutionStrategy {
        force("androidx.test:runner:1.4.0")
    }
}

dependencies {
    implementation(projects.data.marketRepository)
    implementation(libs.kotlinx.collections.immutable)
    implementation(projects.core.uimarket)
    implementation(projects.core.extensions)
    implementation(libs.espresso.core)
    implementation(libs.compose.ui.test.manifest)
    implementation(libs.compose.ui.test.junit4)
}
