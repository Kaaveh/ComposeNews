plugins {
    libs.plugins.apply {
        alias(android.application)
        alias(kotlin.android)
        alias(hilt.android)
        alias(kapt)
    }
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
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.toString()
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(project(":library:sync"))
    implementation(project(":library:base"))
    implementation(project(":library:navigation"))
    implementation(project(":library:designsystem"))
    libs.apply {
        implementation(compose.activity)
        implementation(androidx.ktx)
        implementation(hilt.android)
        kapt(dager.hilt.compiler)
        implementation(hilt.work)
        implementation(lifecycle.runtime.ktx)
        testImplementation(junit)
        androidTestImplementation(junit.ext)
        implementation(work.runtime.ktx)
    }
}

kapt {
    correctErrorTypes = true
}