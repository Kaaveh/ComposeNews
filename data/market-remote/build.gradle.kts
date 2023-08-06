plugins {
    libs.plugins.apply {
        alias(android.library)
        alias(kotlin.android)
        alias(hilt.android)
        alias(kapt)
    }
}

android {
    namespace = "ir.kaaveh.remotedatasource"
    compileSdk = 33

    defaultConfig {
        minSdk = 23

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
}

dependencies {
    libs.apply {
        implementation(retrofit)
        implementation(converter.gson)
        implementation(logging.interceptor)
        debugImplementation(chucker)
        releaseImplementation(chucker.no.op)
    }
    libs.apply {
        implementation(hilt.android)
        kapt(dager.hilt.compiler)
    }
}

kapt {
    correctErrorTypes = true
}