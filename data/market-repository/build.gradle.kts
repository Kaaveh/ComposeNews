plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.data"
    }

    sourceSets {
        commonMain.dependencies {
            projects.apply {
                api(domain.market)
                implementation(data.marketRemote)
                implementation(data.marketLocal)
            }
            libs.apply {
                implementation(kermit)
                implementation(koin.core)
            }
        }
        androidHostTest.dependencies {
            implementation(libs.mockk)
        }
    }
}
