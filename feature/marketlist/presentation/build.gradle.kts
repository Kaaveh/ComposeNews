plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.marketlist"
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
        commonTest.dependencies {
            implementation(projects.core.testing)
            implementation(libs.turbine)
        }
    }
}
