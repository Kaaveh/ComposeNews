plugins {
    libs.plugins.apply {
        alias(android.library)
        alias(kotlin.android)
        alias(hilt.android)
        alias(kapt)
    }
}

android {
    namespace = "ir.kaaveh.sync"
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
}

dependencies {
    api(project(":domain:market"))
    libs.apply {
        implementation(hilt.android)
        implementation(hilt.work)
        kapt(dager.hilt.compiler)
        kapt(android.hilt.compiler)
        implementation(startup.runtime)
        implementation(work.runtime.ktx)
    }
}

kapt {
    correctErrorTypes = true
}