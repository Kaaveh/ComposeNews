plugins {
    libs.plugins.apply {
        alias(android.application) apply false
        alias(kotlin.parcelize) apply false
        alias(android.library) apply false
        alias(kotlin.android) apply false
        alias(hilt.android) apply false
        alias(kapt) apply false
        id("org.jmailen.kotlinter") version "3.16.0" apply false
//        alias(org.jmailen.kotlinter)
        alias(libs.plugins.detekt) apply false
    }
}