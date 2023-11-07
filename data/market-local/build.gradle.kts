plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    libs.plugins.apply {
        alias(sqldelight)
    }
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "ir.composenews.localdatasource"
}

sqldelight {
    databases {
        create("MarketDatabase") {
            packageName.set("ir.composenews.db")
        }
    }
}

dependencies {
    libs.apply {
        implementation(sqldelight.android)
        implementation(sqldelight.coroutines)
        testImplementation(sqldelight.test)
        testImplementation(runner)
    }
    projects.apply {
        testImplementation(core.test)
    }
}