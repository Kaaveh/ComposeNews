package ir.composenews.remotedatasource.test

import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketResponse

class FakeMarketsApi : MarketsApi {
    override suspend fun getMarkets(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean,
    ): List<MarketResponse> = listOf(marketDto)

    override suspend fun getMarketChart(
        id: String,
        currency: String,
        days: Int,
    ): MarketChartResponse {
        TODO("Not yet implemented")
    }
}
