plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.konsist"

}

dependencies {
    libs.apply {
        testImplementation(konsist)
        testImplementation(bundles.kotest)
    }
}