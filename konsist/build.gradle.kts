plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.konsist"

}

dependencies {
    testImplementation(libs.konsist)
    testImplementation(libs.bundles.kotest)
}