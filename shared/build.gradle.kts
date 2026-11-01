import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.composenews.kmp.ios)
    alias(libs.plugins.skie)
}

kotlin {
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(projects.feature.marketlist.presentation)
            export(projects.feature.marketdetail.presentation)
            export(projects.domain.market)
            // Without these, Swift sees `BaseLoadableData` and `UimarketMarketModel`.
            export(projects.core.base)
            export(projects.core.uimarket)
            export(libs.lifecycle.viewmodel)
        }
    }

    sourceSets {
        iosMain.dependencies {
            projects.apply {
                api(feature.marketlist.presentation)
                api(feature.marketdetail.presentation)
                api(domain.market)
                api(core.base)
                api(core.uimarket)
                implementation(core.sync)
                implementation(data.marketRepository)
                implementation(data.marketRemote)
                implementation(data.marketLocal)
            }
            libs.apply {
                api(lifecycle.viewmodel)
                implementation(koin.core)
            }
        }
    }
}

skie {
    analytics { disableUpload.set(true) }
}
