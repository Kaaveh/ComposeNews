package ir.composenews.remotedatasource.test

import ir.composenews.network.ApiResponse
import ir.composenews.remotedatasource.api.MarketsApi
import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse

class FakeMarketsApi : MarketsApi {
    override suspend fun getMarkets(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean,
    ): ApiResponse<List<MarketResponse>> = ApiResponse.Success(listOf(marketDto))

    override suspend fun getMarketChart(
        id: String,
        currency: String,
        days: Int,
    ): ApiResponse<MarketChartResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getMarketDetail(id: String): ApiResponse<MarketDetailResponse> {
        TODO("Not yet implemented")
    }
}
