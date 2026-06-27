pluginManagement {
    repositories {
        if (System.getenv("CI") == null) {
            maven { url = uri("https://maven.myket.ir") }
        }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if (System.getenv("CI") == null) {
            maven { url = uri("https://maven.myket.ir") }
        }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "ComposeNews"
include(":library:designsystem")
include(":library:navigation")
include(":konsist")
include(":core:sync")
include(":core:test")
include(":core:base")
include(":core:uimarket")
include(":core:extensions")
include(":core:network:ktor")
include(":domain:market")
include(":data:market-remote")
include(":data:market-local")
include(":data:market-repository")
include(":feature:marketlist")
include(":feature:marketdetail")
include(":app")
include(":app-watch")
include(":app-watch:app")
include(":app-watch:navigation")
include(":app-watch:ui")
include(":app-tv")
include(":app-tv:app")
include(":app-tv:navigation")
include(":app-tv:ui")
include(":data:sqldelight")
//include(":baselineprofile")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    Compose News project requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
