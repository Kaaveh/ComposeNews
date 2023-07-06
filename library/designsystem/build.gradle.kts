plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "ir.kaaveh.designsystem"
    compileSdk = projectCompileSdkVersion

    defaultConfig {
        minSdk = projectMinSdkVersion

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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
        api(composeCoil)
    }
    LifeCycleDependencies.apply {
        api(lifeCycleRuntimeCompose)
    }
}