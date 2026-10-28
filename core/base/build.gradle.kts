plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.base"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.network.ktor)
            libs.apply {
                api(lifecycle.viewmodel)
                implementation(koin.core)
            }
        }
    }
}
