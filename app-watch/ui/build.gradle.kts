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
    projects.apply {
        implementation(feature.marketlist)
        implementation(feature.marketdetail)
        implementation(core.uimarket)
        implementation(core.extensions)
        implementation(data.marketRepository)
    }
    libs.apply {
        implementation(compose.ui.preview.wear)
        implementation(compose.foundation.wear)
        implementation(compose.horologist.layout)
        implementation(compose.ui.test.manifest)
        implementation(compose.ui.test.junit4)
    }
}
