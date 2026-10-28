plugins {
    alias(libs.plugins.composenews.kmp.library)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "ir.composenews.uimarket"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.market)
            api(libs.kotlinx.serialization.json)
        }
    }
}
