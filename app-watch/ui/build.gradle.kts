plugins {
    id("composenews.android.feature")
}

android {
    namespace = "ir.composenews.app_watch.ui"
}

configurations.all {
    resolutionStrategy {
        force("androidx.test:runner:1.4.0")
    }
}

dependencies {

    projects.appWatch.apply {
        implementation(designsystem)
    }

    projects.feature.apply {
        implementation(marketlist)
        implementation(marketdetail)
    }

    implementation(projects.data.marketRepository)
    implementation(projects.core.uimarket)
    implementation(projects.core.extensions)
    implementation(libs.compose.ui.preview.wear)
    implementation(libs.compose.foundation.wear)
    implementation(libs.compose.horologist.layout)
    implementation(libs.compose.ui.test.manifest)
    implementation(libs.compose.ui.test.junit4)
}
