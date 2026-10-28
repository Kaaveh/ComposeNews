@file:Suppress("ForbiddenComment")

plugins {
    alias(libs.plugins.composenews.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = libs.versions.projectApplicationId.get()

    defaultConfig {
        applicationId = libs.versions.projectApplicationId.get()

        testInstrumentationRunner = "ir.composenews.ui.InstrumentationTestRunner"
    }

    buildTypes {
        getByName("release") {
            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
        }
    }

    // Use a separate build variant to be able to provide a mock version of MarketApi for
    // benchmark build
    flavorDimensions += "backend"
    productFlavors {
        create("live") {
            dimension = "backend"
        }

        create("fixture") {
            dimension = "backend"
        }
    }
}

baselineProfile {
    hideSyntheticBuildTypesInAndroidStudio = true

    variants {
        create("fixtureRelease") {
            mergeIntoMain = true
            from(project(":baselineprofile"))
        }
    }
}

dependencies {
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.workmanager)

    implementation(libs.profileinstaller)

    projects.apply {
        implementation(library.navigation)
        implementation(library.designsystem)
        implementation(core.sync)
        implementation(core.base)
        implementation(core.uimarket)
        implementation(domain.market)
        implementation(data.marketLocal)
        implementation(data.marketRemote)
        implementation(data.marketRepository)
        implementation(feature.marketlist)
        implementation(feature.marketdetail)
        androidTestImplementation(testFixtures(core.test))
    }
    libs.apply {
        implementation(compose.activity)
        implementation(androidx.ktx)
        implementation(lifecycle.runtime)
        implementation(work.runtime.ktx)
        implementation(navigation3.runtime)
        implementation(navigation3.ui)
        implementation(lifecycle.viewmodel.navigation3)
        implementation(compose.material3.adaptive.navigation.suite)
        implementation(compose.material3.adaptive.navigation)
        implementation(compose.ui.test.manifest)
        implementation(compose.ui.test.junit4)
        androidTestImplementation(mockk.android)
        androidTestImplementation(rules)
    }
}
