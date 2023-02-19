plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = projectApplicationId
    compileSdk = projectCompileSdkVersion

    defaultConfig {
        minSdk = projectMinSdkVersion
        targetSdk = projectTargetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ComposeDependencies.kotlinCompilerExtensionVersion
    }
}

dependencies {
    ComposeDependencies.apply {
        api(platform(composeBOM))
        api(composeUi)
        api(composeUiPreview)
        api(composeUiTooling)
        api(composeUiTestManifest)
        api(composeMaterial)
        implementation(composeCoil)
    }
    LifeCycleDependencies.apply {
        api(lifeCycleRuntimeCompose)
        implementation(lifeCycleViewModelKtx)
    }
}