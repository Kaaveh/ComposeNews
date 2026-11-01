plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.sync"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(projects.domain.market)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            libs.apply {
                implementation(koin.android)
                implementation(koin.workmanager)
                implementation(startup.runtime)
                implementation(work.runtime.ktx)
            }
        }
    }
}
