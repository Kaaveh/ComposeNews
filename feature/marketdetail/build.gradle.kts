plugins {
    alias(libs.plugins.composenews.android.feature)
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
        testImplementation(turbine)
    }
}
