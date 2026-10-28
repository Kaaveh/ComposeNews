plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.ktor"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
        }
    }
}
