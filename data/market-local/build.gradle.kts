plugins {
    alias(libs.plugins.composenews.kmp.room)
}

kotlin {
    android {
        namespace = "ir.composenews.localdatasource"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.base)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        androidHostTest.dependencies {
            libs.apply {
                implementation(junit.ext)
                implementation(robolectric)
                runtimeOnly(junit.vintage.engine)
            }
        }
    }
}
