plugins {
    libs.plugins.apply {
        alias(android.library)
        alias(kotlin.android)
        alias(hilt.android)
        alias(kapt)
    }
}

android {
    namespace = "ir.kaaveh.newsdetail"
    compileSdk = projectCompileSdkVersion

    defaultConfig {
        minSdk = projectMinSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    api(project(":library:designsystem"))
    api(project(":library:base"))
    implementation(project(":data:market-repository"))
    libs.apply {
        implementation(hilt.android)
        kapt(dager.hilt.compiler)
        implementation(hilt.navigation.compose)
        implementation(accompanist.webview)
    }
}

kapt {
    correctErrorTypes = true
}