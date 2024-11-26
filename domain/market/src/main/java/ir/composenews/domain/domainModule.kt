package ir.composenews.domain

import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.GetMarketListUseCase
import ir.composenews.domain.use_case.SyncMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::GetFavoriteMarketListUseCase)
    factoryOf(::GetMarketChartUseCase)
    factoryOf(::GetMarketDetailUseCase)
    factoryOf(::GetMarketListUseCase)
    factoryOf(::SyncMarketListUseCase)
    factoryOf(::ToggleFavoriteMarketListUseCase)
}