plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "ir.kaaveh.remotedatasource"
    compileSdk = projectCompileSdkVersion

    defaultConfig {
        minSdk = projectMinSdkVersion
        targetSdk = projectTargetSdkVersion

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":domain:news"))
    NetworkDependencies.apply {
        implementation(retrofit)
        implementation(gson)
        implementation(loggingInterceptor)
        debugImplementation(chucker)
        releaseImplementation(chuckerNoOp)
    }
    DIDependencies.apply {
        implementation(hiltAndroid)
        kapt(dagerHiltCompiler)
    }
    TestDependencies.apply {
        testImplementation(junit)
        androidTestImplementation(junitExt)
    }
}

kapt {
    correctErrorTypes = true
}