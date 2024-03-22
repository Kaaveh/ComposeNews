plugins {
    id("composenews.android.feature")
}

android {
    namespace = "ir.composenews.newsdetail"
}

configurations.all {
    resolutionStrategy {
        force("androidx.test:runner:1.4.0" )
    }
}

dependencies {
    implementation(projects.data.marketRepository)
    implementation(projects.core.uimarket)
    implementation(libs.espresso.core)
    implementation(libs.compose.ui.test.manifest)
    implementation(libs.compose.ui.test.junit4)
}