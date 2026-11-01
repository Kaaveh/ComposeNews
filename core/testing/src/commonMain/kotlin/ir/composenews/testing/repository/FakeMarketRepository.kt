package ir.composenews.testing.repository

import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** A [MarketRepository] whose answers tests swap per method, recording the calls they assert on. */
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

    var marketList: () -> Flow<List<Market>> = { flowOf(markets) }
    var favoriteList: () -> Flow<List<Market>> = { flowOf(emptyList()) }
    var marketById: (id: String) -> Flow<Market?> = { id -> flowOf(markets.firstOrNull { it.id == id }) }
    var sync: suspend () -> Resource<Unit, Errors> = { Resource.Success(Unit) }
    var chart: (id: String, days: Int) -> Flow<Resource<MarketChart, Errors>> = { _, _ ->
        flowOf(Resource.Success(MarketChart(persistentListOf(START to END))))
    }
    var detail: (id: String) -> Flow<Resource<MarketDetail, Errors>> = { id ->
        flowOf(Resource.Success(bitcoinDetail(id)))
    }

    var favoriteListCalls = 0
        private set
    var syncCalls = 0
        private set
    val toggled = mutableListOf<Market>()
    val chartCalls = mutableListOf<Pair<String, Int>>()
    val detailCalls = mutableListOf<String>()

    override fun getMarketList(): Flow<List<Market>> = marketList()

    override fun getFavoriteMarketList(): Flow<List<Market>> {
        favoriteListCalls++
        return favoriteList()
    }

    override fun getMarketById(id: String): Flow<Market?> = marketById(id)

    override suspend fun syncMarketList(): Resource<Unit, Errors> {
        syncCalls++
        return sync()
    }

    override suspend fun toggleFavoriteMarket(oldMarket: Market) {
        toggled += oldMarket
    }

    override fun fetchChart(
        id: String,
        days: Int,
    ): Flow<Resource<MarketChart, Errors>> {
        chartCalls += id to days
        return chart(id, days)
    }

    override fun fetchDetail(id: String): Flow<Resource<MarketDetail, Errors>> {
        detailCalls += id
        return detail(id)
    }

    private fun bitcoinDetail(id: String) =
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
        )

    companion object {
        const val START = 0L
        const val END = 100000.0
    }
}
