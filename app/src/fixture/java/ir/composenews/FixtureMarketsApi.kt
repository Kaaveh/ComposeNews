package ir.composenews

import ir.composenews.network.ApiResponse
import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse

// Fixed numeric values keep benchmark fixture data deterministic.
@Suppress("MagicNumber")
internal class FixtureMarketsApi : MarketsApi {
    // There are 60 entries so the LazyColumn is
    // guaranteed to be scrollable.
    private val markets =
        (1..60).map { index ->
            MarketResponse(
                id = "market-$index",
                name = "Market $index",
                symbol = "m$index",
                currentPrice = 1_000.0 + index,
                priceChangePercentage24h = index / 10.0,
                imageUrl = "",
            )
        }

    override suspend fun getMarkets(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean,
    ): ApiResponse<List<MarketResponse>> {
        val offset = ((page - 1) * perPage).coerceAtLeast(0)
        val pageItems = markets.drop(offset).take(perPage)
        return ApiResponse.Success(pageItems)
    }

    override suspend fun getMarketChart(
        id: String,
        currency: String,
        days: Int,
    ): ApiResponse<MarketChartResponse> =
        ApiResponse.Success(
            MarketChartResponse(
                prices =
                    listOf(
                        listOf(1_000.0, 50_000.0),
                        listOf(2_000.0, 51_000.0),
                    ),
            ),
        )

    override suspend fun getMarketDetail(id: String): ApiResponse<MarketDetailResponse> =
        ApiResponse.Success(
            MarketDetailResponse(
                id = id,
                marketCapRank = 1,
                name = "Fixture Market",
                marketData =
                    MarketDetailResponse.MarketData(
                        high24h = MarketDetailResponse.MarketData.High24h(51_000.0),
                        low24h = MarketDetailResponse.MarketData.Low24h(49_000.0),
                        marketCap = MarketDetailResponse.MarketData.MarketCap(1_000_000_000L),
                        marketCapRank = 1,
                    ),
            ),
        )
}
