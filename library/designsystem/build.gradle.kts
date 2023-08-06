plugins {
    libs.plugins.apply {
        alias(android.library)
        alias(kotlin.android)
    }
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    libs.apply {
        api(platform(compose.bom))
        api(compose.ui)
        api(compose.ui.preview)
        api(compose.ui.tooling)
        api(compose.ui.test.manifest)
        api(compose.material)
        api(compose.coil)

        api(lifecycle.runtime.compose)
    }
}