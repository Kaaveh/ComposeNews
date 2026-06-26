package ir.composenews.ui

import androidx.paging.PagingData
import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.repository.FakeMarketRepository
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetMarketByIdUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.GetPagedMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.marketdetail.MarketDetailViewModel
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
