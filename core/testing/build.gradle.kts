plugins {
    alias(libs.plugins.composenews.kmp.library)
}

kotlin {
    android {
        namespace = "ir.composenews.testing"
    }

    sourceSets {
        commonMain.dependencies {
            libs.apply {
                api(kotest.frameworkEngine)
                api(kotest.assertionsCore)
                api(coroutines.test)
            }
        }
        // These modules are Android-only, so the fixtures built on them live in androidMain.
        // Move them to commonMain once the modules are KMP (#359 and on).
        androidMain.dependencies {
            projects.apply {
                api(core.base)
                api(core.uimarket)
                api(domain.market)
            }
        }
    }
}
