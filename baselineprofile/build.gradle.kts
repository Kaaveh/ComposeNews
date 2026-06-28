import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.android.built.in1.kotlin)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "ir.composenews.baselineprofile"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        minSdk = 28
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

//    testOptions.managedDevices.allDevices {
//        create<ManagedVirtualDevice>("mediumPhoneApi36") {
//            device = "Medium Phone"
//            apiLevel = 36
//            systemImageSource = "aosp"
//        }
//    }

    flavorDimensions += "backend"
    productFlavors {
        create("fixture") {
            dimension = "backend"
        }
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    //managedDevices += "mediumPhoneApi36"
    useConnectedDevices = true
}

dependencies {
    implementation(libs.junit.ext)
    implementation(libs.androidx.espresso.core)
    implementation(libs.test.uiautomator)
    implementation(libs.benchmark.macro.junit4)
}

androidComponents {
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId }
        )
    }
}