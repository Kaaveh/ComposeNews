plugins {
    `kotlin-dsl`
}

group = "ir.composenews.buildlogic"

// compileOnly: the root build script puts the real plugins on the classpath (`apply false`).
dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.composenews.android.application.get().pluginId
            implementationClass = "ir.composenews.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.composenews.android.library.asProvider().get().pluginId
            implementationClass = "ir.composenews.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.composenews.android.library.compose.get().pluginId
            implementationClass = "ir.composenews.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("kmpLibrary") {
            id = libs.plugins.composenews.kmp.library.get().pluginId
            implementationClass = "ir.composenews.buildlogic.KmpLibraryConventionPlugin"
        }
        register("kmpRoom") {
            id = libs.plugins.composenews.kmp.room.get().pluginId
            implementationClass = "ir.composenews.buildlogic.KmpRoomConventionPlugin"
        }
    }
}
