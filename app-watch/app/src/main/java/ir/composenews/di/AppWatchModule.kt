package ir.composenews.di

import ir.composenews.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appWatchModule =
    module {
        viewModel { MainViewModel(get()) }
    }
