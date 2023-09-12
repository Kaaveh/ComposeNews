object ComposeDependencies {
    object Version {
        const val composeBOM = "2023.06.01"
        const val composeActivity = "1.6.1"
        const val composeCoil = "2.2.2"
        const val accompanist = "0.24.11-rc"
        const val adaptive = "0.26.2-beta"
    }

    const val kotlinCompilerExtensionVersion = "1.4.2"
    const val composeBOM = "androidx.compose:compose-bom:${Version.composeBOM}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest"
    const val composeMaterial = "androidx.compose.material:material"
    const val composeActivity = "androidx.activity:activity-compose:${Version.composeActivity}"
    const val composeCoil = "io.coil-kt:coil-compose:${Version.composeCoil}"
    const val accompanistWebview =
        "com.google.accompanist:accompanist-webview:${Version.accompanist}"
    const val windowSizeClass = "androidx.compose.material3:material3-window-size-class"
    const val accompanistAdaptive = "com.google.accompanist:accompanist-adaptive:${Version.adaptive}"

}