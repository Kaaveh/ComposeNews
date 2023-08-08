plugins {
    id("composenews.android.library")
    id("composenews.android.library.compose")
    id("composenews.android.hilt")
}

android {
    namespace = "ir.kaaveh.newsdetail"
}

dependencies {
    api(project(":library:designsystem"))
    api(project(":library:base"))
    implementation(project(":data:market-repository"))
    libs.apply {
        implementation(hilt.navigation.compose)
        implementation(accompanist.webview)
    }
}