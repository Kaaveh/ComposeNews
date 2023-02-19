plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "ir.kaaveh.designsystem"
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

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
        kotlinCompilerExtensionVersion = "1.4.2"
    }
}

dependencies {
    api("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha03")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    api(platform("androidx.compose:compose-bom:2023.01.00"))
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-tooling-preview")
    api("androidx.compose.material:material")
    implementation("io.coil-kt:coil-compose:2.2.2")
    debugApi("androidx.compose.ui:ui-tooling")
    debugApi("androidx.compose.ui:ui-test-manifest")
}