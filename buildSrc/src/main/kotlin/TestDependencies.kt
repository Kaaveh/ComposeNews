object TestDependencies {
    object Versions {
        const val junit = "4.13.2"
        const val junitExt = "1.1.5"
        const val coroutinesTest = "1.6.4"
        const val mockitoKotlin = "4.1.0"
        const val mockk = "1.13.5"
        const val runner = "1.4.0"
    }

    const val junit = "junit:junit:${Versions.junit}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
    const val mokitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val runner = "androidx.test:runner:${Versions.runner}"
}
