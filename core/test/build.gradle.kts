plugins {
    alias(libs.plugins.composenews.android.library)
}

android {
    namespace = "ir.composenews.core_test"

    testFixtures {
        enable = true
    }
}

dependencies {
    libs.apply {
        api(bundles.kotest)
        api(koin.core)
        testFixturesApi(bundles.kotest)
        testFixturesApi(coroutines.test)
        testFixturesApi(mockk)
    }
    projects.apply {
        api(core.base)
        testFixturesApi(core.uimarket)
        testFixturesApi(domain.market)
    }
}
