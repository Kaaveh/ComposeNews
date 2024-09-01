plugins {
    alias(libs.plugins.composenews.android.library)
    libs.plugins.apply {
        alias(kotlinx.serialization)
    }
}

android {
    namespace = "ir.composenews.ktor"

}

dependencies {
    libs.apply {
        implementation(bundles.ktor)
    }
}