plugins {
    libs.plugins.apply {
        alias(android.application) apply false
        alias(kotlin.parcelize) apply false
        alias(android.library) apply false
        alias(kotlin.android) apply false
        alias(hilt.android) apply false
        alias(kapt) apply false
        alias(detekt) apply false
        alias(kotliner) apply false
    }
}