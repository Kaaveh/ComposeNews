object DIDependencies {
    object Version {
        const val hilt = "2.44.2"
        const val hiltNavigationCompose = "1.0.0"
        const val hiltWork = "1.0.0"
    }

    const val hiltAndroid = "com.google.dagger:hilt-android:${Version.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Version.hilt}"
    const val hiltNavigationCompose =
        "androidx.hilt:hilt-navigation-compose:${Version.hiltNavigationCompose}"
    const val hiltWorke = "androidx.hilt:hilt-work:${Version.hiltWork}"

}