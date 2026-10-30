plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.sync"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(projects.domain.market)
            libs.apply {
                implementation(koin.android)
                implementation(koin.workmanager)
                implementation(startup.runtime)
                implementation(work.runtime.ktx)
            }
        }
    }
}
