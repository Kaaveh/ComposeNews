package ir.composenews.marketdetail.di

import ir.composenews.marketdetail.MarketDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val marketDetailModule =
    module {
        viewModel { MarketDetailViewModel(get(), get(), get(), get(), get()) }
    }
