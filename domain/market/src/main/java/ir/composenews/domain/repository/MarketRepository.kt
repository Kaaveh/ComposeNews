@file:Suppress(
    "ktlint:standard:blank-line-before-declaration",
)

package ir.composenews.domain.repository

import androidx.paging.PagingData
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface MarketRepository {
    fun getMarketList(): Flow<List<Market>>
    fun getPagedMarketList(): Flow<PagingData<Market>>
    fun getFavoriteMarketList(): Flow<List<Market>>
    fun getMarketById(id: String): Flow<Market?>
    suspend fun syncMarketList()
    suspend fun toggleFavoriteMarket(oldMarket: Market)
    fun fetchChart(id: String): Flow<Resource<MarketChart, Errors>>
    fun fetchDetail(id: String): Flow<Resource<MarketDetail, Errors>>
}

class FakeMarketRepository : MarketRepository {
    private val markets =
        listOf(
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
        flowOf(Resource.Success(MarketChart(persistentListOf(START to END))))

    override fun fetchDetail(id: String): Flow<Resource<MarketDetail, Errors>> =
        flowOf(
            Resource.Success(
                MarketDetail(
                    id = id,
                    marketCapRank = 1,
                    marketData =
                        MarketDetail.MarketData(
                            high24hUSD = 101000.0,
                            low24hUSD = 99000.0,
                            marketCapUSD = 1_000_000_000L,
                            marketCapRank = 1,
                        ),
                    name = "Bitcoin",
                ),
            ),
        )

        companion object {
            const val START = 0L 
            const val END = 100000.0
        }
}
