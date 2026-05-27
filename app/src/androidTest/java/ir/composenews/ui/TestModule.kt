package ir.composenews.ui

import androidx.paging.PagingData
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.domain.use_case.GetMarketByIdUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
import ir.composenews.domain.use_case.GetPagedMarketListUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.marketdetail.MarketDetailViewModel
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val testModule = module {
    single<MarketRepository> { FakeMarketRepository() }
    factory { GetPagedMarketListUseCase(get()) }
    factory { GetFavoriteMarketListUseCase(get()) }
    factory { ToggleFavoriteMarketListUseCase(get()) }
    factory { GetMarketByIdUseCase(get()) }
    factory { GetMarketChartUseCase(get()) }
    factory { GetMarketDetailUseCase(get()) }
    single<DispatcherProvider> {
        object : DispatcherProvider {
            override val ui: CoroutineDispatcher = Dispatchers.Unconfined
            override val io: CoroutineDispatcher = Dispatchers.Unconfined
            override val bg: CoroutineDispatcher = Dispatchers.Unconfined
        }
    }
    viewModel { MarketListViewModel(get(), get(), get(), get()) }
    viewModel { MarketDetailViewModel(get(), get(), get(), get(), get()) }
}

private class FakeMarketRepository : MarketRepository {
    private val markets = listOf(
        Market(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 100000.0,
            priceChangePercentage24h = 2.5,
            imageUrl = "",
        ),
    )

    override fun getMarketList(): Flow<List<Market>> = flowOf(markets)

    override fun getPagedMarketList(): Flow<PagingData<Market>> = flowOf(PagingData.from(markets))

    override fun getFavoriteMarketList(): Flow<List<Market>> = flowOf(emptyList())

    override fun getMarketById(id: String): Flow<Market?> = flowOf(markets.firstOrNull { it.id == id })

    override suspend fun syncMarketList() = Unit

    override suspend fun toggleFavoriteMarket(oldMarket: Market) = Unit

    override fun fetchChart(id: String): Flow<Resource<MarketChart, Errors>> =
        flowOf(Resource.Success(MarketChart(persistentListOf(0L to 100000.0))))

    override fun fetchDetail(id: String): Flow<Resource<MarketDetail, Errors>> =
        flowOf(
            Resource.Success(
                MarketDetail(
                    id = id,
                    marketCapRank = 1,
                    marketData = MarketDetail.MarketData(
                        high24hUSD = 101000.0,
                        low24hUSD = 99000.0,
                        marketCapUSD = 1_000_000_000L,
                        marketCapRank = 1,
                    ),
                    name = "Bitcoin",
                ),
            ),
        )
}
