package ir.composenews.ui

import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.core_test.repository.FakeMarketRepository
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketByIdUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.GetPagedMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.marketdetail.MarketDetailViewModel
import ir.composenews.marketlist.MarketListViewModel
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val testModule =
    module {
        single<MarketRepository> { FakeMarketRepository() }
        factory { GetPagedMarketListUseCase(get()) }
        factory { GetFavoriteMarketListUseCase(get()) }
        factory { ToggleFavoriteMarketListUseCase(get()) }
        factory { GetMarketByIdUseCase(get()) }
        factory { GetMarketChartUseCase(get()) }
        factory { GetMarketDetailUseCase(get()) }
        single<DispatcherProvider> {
            TestDispatcherProvider(TestCoroutineScheduler())
        }
        viewModel { MarketListViewModel(get(), get(), get(), get()) }
        viewModel { MarketDetailViewModel(get(), get(), get(), get(), get()) }
    }
