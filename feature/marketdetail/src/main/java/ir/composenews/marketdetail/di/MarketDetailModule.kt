package ir.composenews.marketdetail.di

import ir.composenews.core_test.di.dispatcherModule
import ir.composenews.data.di.dataModule
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.marketdetail.MarketDetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val marketDetailModule =
    module {

        includes(
            dataModule,
            dispatcherModule,
        )

        factoryOf(::GetMarketChartUseCase)
        factoryOf(::GetMarketDetailUseCase)
        factoryOf(::ToggleFavoriteMarketListUseCase)
        viewModelOf(::MarketDetailViewModel)
    }
