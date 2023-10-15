plugins {
    `kotlin-dsl`
}

group = "ir.composenews.composenews.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.ktlint.kotlinter)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "composenews.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "composenews.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidHilt") {
            id = "composenews.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidLibrary") {
            id = "composenews.android.library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "composenews.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "composenews.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidRoom") {
            id = "composenews.android.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("androidDetekt") {
            id = "composenews.android.detekt"
            implementationClass = "AndroidDetektConventionPlugin"
        }
        register("androidKtlint") {
            id = "composenews.android.ktlint"
            implementationClass = "AndroidKotlinterConventionPlugin"
        }
    }
}
