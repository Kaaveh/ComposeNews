plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.marketdetail"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.base)
            projects.apply {
                implementation(domain.market)
                implementation(core.uimarket)
            }
            libs.apply {
                implementation(koin.core)
                implementation(koin.core.viewmodel)
            }
        }
        androidHostTest.dependencies {
            implementation(projects.core.testing)
            libs.apply {
                implementation(mockk)
                implementation(turbine)
            }
        }
    }
}
