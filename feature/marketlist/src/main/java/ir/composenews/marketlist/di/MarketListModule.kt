package ir.composenews.marketlist.di

import ir.composenews.marketlist.MarketListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val marketListModule =
    module {
        viewModel { MarketListViewModel(get(), get(), get(), get()) }
    }
