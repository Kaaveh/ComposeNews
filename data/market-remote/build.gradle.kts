plugins {
    alias(libs.plugins.composenews.kmp.library)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "ir.composenews.remotedatasource"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.network.ktor)
            implementation(projects.core.base)
            libs.apply {
                implementation(koin.core)
                implementation(ktor.contentNegotiation)
                implementation(ktor.json)
                implementation(ktor.logging)
            }
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        androidHostTest.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}
