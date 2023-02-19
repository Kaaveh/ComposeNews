plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = projectApplicationId
    compileSdk = projectCompileSdkVersion

    defaultConfig {
        applicationId = projectApplicationId
        minSdk = projectMinSdkVersion
        targetSdk = projectTargetSdkVersion
        versionCode = projectVersionCode
        versionName = projectVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":sync"))
    implementation(project(":library:navigation"))
    implementation(project(":library:designsystem"))
    DIDependencies.apply {
        implementation(hiltAndroid)
        kapt(hiltCompiler)
        implementation(hiltWorke)
    }
    TestDependencies.apply {
        testImplementation(junit)
        androidTestImplementation(junitExt)
    }
    implementation(ComposeDependencies.composeActivity)
    implementation(LifeCycleDependencies.lifeCycleRuntimeKtx)
    implementation(AndroidxDependencies.coreKtx)
    implementation("androidx.work:work-runtime-ktx:2.8.0")
}

kapt {
    correctErrorTypes = true
}