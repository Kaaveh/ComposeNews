plugins {
    id("composenews.android.feature")
}

android {
    namespace = "ir.composenews.newsdetail"
}

dependencies {
    implementation(projects.data.marketRepository)
}