plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "ir.kaaveh.data"
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
    api(project(":domain:news"))
    implementation(project(":data:news-remote"))
    implementation(project(":data:news-local"))
    implementation(LifeCycleDependencies.lifeCycleViewModelKtx)
    DIDependencies.apply {
        implementation(hiltAndroid)
        kapt(dagerHiltCompiler)
    }
    RoomDependencies.apply {
        implementation(roomRuntime)
        implementation(roomKtx)
        kapt(roomCompiler)
    }
    TestDependencies.apply {
        testImplementation(junit)
        androidTestImplementation(junitExt)
        androidTestImplementation(coroutinesTest)
    }
}

kapt {
    correctErrorTypes = true
}