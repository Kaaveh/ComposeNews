plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
}

android {
    namespace = "ir.kaaveh.base"
}

dependencies {
    projects.library.apply {
        api(projects.library.coreTest)
        implementation(projects.library.designsystem)
    }
    libs.apply {
        implementation(lifecycle.viewmodel.ktx)
    }
}