plugins {
    id("composenews.android.library")
    id("composenews.android.hilt")
    id("composenews.android.room")
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
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        androidTestImplementation(coroutines.test)
        androidTestImplementation(runner)
    }
}