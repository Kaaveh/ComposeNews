package ir.composenews.marketlist.di

import ir.composenews.core_test.di.dispatcherModule
import ir.composenews.data.di.dataModule
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.marketlist.MarketListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val marketListModule =
    module {
        includes(
            dataModule,
            dispatcherModule,
        )

        factoryOf(::GetMarketListUseCase)
        factoryOf(::GetFavoriteMarketListUseCase)
        factoryOf(::ToggleFavoriteMarketListUseCase)
        viewModelOf(::MarketListViewModel)
    }
