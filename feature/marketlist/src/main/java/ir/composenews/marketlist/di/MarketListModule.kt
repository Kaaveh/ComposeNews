package ir.composenews.marketlist.di

import ir.composenews.marketlist.MarketListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val marketListModule =
    module {
        viewModel { MarketListViewModel(get(), get(), get(), get()) }
    }
