package ir.kaaveh.remotedatasource.test

import ir.kaaveh.remotedatasource.api.MarketsApi
import ir.kaaveh.remotedatasource.dto.MarketResponse

class FakeMarketsApi : MarketsApi {
    override suspend fun getMarkets(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean
    ): List<MarketResponse> = listOf(marketDto)
}