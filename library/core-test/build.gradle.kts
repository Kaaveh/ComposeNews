plugins {
    alias(libs.plugins.android.library)
    id("kotlin-android")
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "ir.kaaveh.core_test"
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
}

dependencies {
    libs.apply {
        api(junit)
        api(junit.ext)
        api(coroutines.test)
        api(mockk)
        implementation(hilt.android)
        kapt(dager.hilt.compiler)
    }
}