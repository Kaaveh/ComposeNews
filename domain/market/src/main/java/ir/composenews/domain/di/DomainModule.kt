package ir.composenews.domain.di

import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.GetPagedMarketListUseCase
import ir.composenews.domain.use_case.SyncMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import org.koin.dsl.module

val domainModule =
    module {
        factory { GetFavoriteMarketListUseCase(get()) }
        factory { GetMarketChartUseCase(get()) }
        factory { GetMarketDetailUseCase(get()) }
        factory { GetMarketListUseCase(get()) }
        factory { GetPagedMarketListUseCase(get()) }
        factory { SyncMarketListUseCase(get()) }
        factory { ToggleFavoriteMarketListUseCase(get()) }
    }
