package ir.composenews.data.repository

import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketEntity
import ir.composenews.network.ApiResponse
import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** Answers with an empty top-markets list until a test swaps in its own answer. */
class FakeMarketsApi : MarketsApi {
    var markets: suspend () -> ApiResponse<List<MarketResponse>> = { ApiResponse.Success(emptyList()) }
    var chart: suspend () -> ApiResponse<MarketChartResponse> = { error("chart not stubbed") }
    var detail: suspend () -> ApiResponse<MarketDetailResponse> = { error("detail not stubbed") }

    val marketsCalls = mutableListOf<List<Any>>()
    val chartCalls = mutableListOf<Triple<String, String, Int>>()
    val detailCalls = mutableListOf<String>()

    override suspend fun getMarkets(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean,
    ): ApiResponse<List<MarketResponse>> {
        marketsCalls += listOf(currency, order, perPage, page, sparkline)
        return markets()
    }

    override suspend fun getMarketChart(
        id: String,
        currency: String,
        days: Int,
    ): ApiResponse<MarketChartResponse> {
        chartCalls += Triple(id, currency, days)
        return chart()
    }

    override suspend fun getMarketDetail(id: String): ApiResponse<MarketDetailResponse> {
        detailCalls += id
        return detail()
    }
}

/** Records the writes the repository makes; Room's own SQL is covered by `MarketDaoChecks`. */
class FakeMarketDao : MarketDao {
    var markets: Flow<List<MarketEntity>> = flowOf(emptyList())
    var favorites: Flow<List<MarketEntity>> = flowOf(emptyList())

    val replaced = mutableListOf<List<MarketEntity>>()
    val favoriteUpdates = mutableListOf<Pair<String, Long>>()

    override fun getMarketList() = markets

    override fun getFavoriteMarketList() = favorites

    override fun getMarketById(id: String): Flow<MarketEntity?> = flowOf(null)

    override suspend fun insertOrIgnoreMarket(marketEntity: MarketEntity) = Unit

    override suspend fun updateMarketData(
        id: String,
        name: String,
        symbol: String,
        currentPrice: Double,
        priceChangePercentage24h: Double,
        imageUrl: String,
        marketCapRank: Int?,
        totalVolume: Double?,
        sparkline7d: String?,
    ) = Unit

    override suspend fun updateFavoriteStatus(
        id: String,
        isFavorite: Long,
    ) {
        favoriteUpdates += id to isFavorite
    }

    override suspend fun clearMarketCapRanks() = Unit

    override suspend fun replaceTopMarkets(marketEntities: List<MarketEntity>) {
        replaced += marketEntities
    }
}
