object LifeCycleDependencies {
    object Versions {
        const val lifeCycleVersion = "2.5.1"
        const val lifeCycleViewModelKtx = "2.5.1"
        const val lifeCycleRuntimeCompose = "2.6.0-beta01"
    }

    const val lifeCycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifeCycleVersion}"
    const val lifeCycleRuntimeCompose =
        "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifeCycleRuntimeCompose}"
    const val lifeCycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycleViewModelKtx}"
}