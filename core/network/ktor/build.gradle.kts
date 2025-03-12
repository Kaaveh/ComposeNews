plugins {
    alias(libs.plugins.composenews.android.library)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ir.composenews.ktor"

}

dependencies {
    libs.apply {
        implementation(bundles.ktor)
    }
}
