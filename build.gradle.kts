plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    kotlin("android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44.2" apply false
}