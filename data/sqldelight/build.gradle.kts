plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.composenews.android.hilt)
    alias(libs.plugins.sqldelight)
}
android {
    namespace = "ir.composenews.sqldelight"
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
        api(sqldelight.android)
        api(sqldelight.coroutines)
        api(sqldelight.test)
        api(runner)
    }
    projects.apply {
        testImplementation(core.test)
    }
}
