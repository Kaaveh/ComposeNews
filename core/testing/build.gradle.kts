plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.testing"
    }

    sourceSets {
        commonMain.dependencies {
            projects.apply {
                api(core.base)
                api(core.uimarket)
                api(domain.market)
            }
            libs.apply {
                api(kotest.frameworkEngine)
                api(kotest.assertionsCore)
                api(coroutines.test)
            }
        }
    }
}
