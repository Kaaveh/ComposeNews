plugins {
    id("composenews.android.feature.presentation")
}

android {
    namespace = "ir.kaaveh.newsdetail"
}

dependencies {
    implementation(project(":data:market-repository"))
    libs.apply {
        implementation(accompanist.webview)
    }
}