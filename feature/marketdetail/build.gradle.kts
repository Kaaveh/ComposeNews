plugins {
    id("composenews.android.feature")
}

android {
    namespace = "ir.composenews.newsdetail"
}

configurations.all {
    resolutionStrategy {
        force("androidx.test:runner:1.4.0")
    }
}

dependencies {
    projects.apply {
        implementation(data.marketRepository)
        implementation(core.uimarket)
    }
    libs.apply {
        implementation(espresso.core)
        implementation(compose.ui.test.manifest)
        implementation(compose.ui.test.junit4)
    }
}