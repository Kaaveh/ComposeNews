object ComposeDependencies {

    object Version {
        const val composeBOM = "2023.01.00"
        const val composeActivity = "1.6.1"
        const val composeCoil = "2.2.2"
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

}