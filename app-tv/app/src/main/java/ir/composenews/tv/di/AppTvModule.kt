package ir.composenews.tv.di

import ir.composenews.tv.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appTvModule = module { viewModel { MainViewModel(get()) } }
