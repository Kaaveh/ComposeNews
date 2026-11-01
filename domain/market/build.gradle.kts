plugins {
    alias(libs.plugins.composenews.kmp.library)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "ir.composenews.domain"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.network.ktor)
            libs.apply {
                implementation(koin.core)
                api(kotlinx.collections.immutable)
                api(kotlinx.serialization.json)
            }
        }
        commonTest.dependencies {
            implementation(projects.core.testing)
        }
    }
}
