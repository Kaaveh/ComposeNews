package ir.composenews

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModelModule =module {
    viewModel {
        MainViewModel(get())}
}
