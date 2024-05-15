@file:Suppress("TopLevelPropertyNaming")

package ir.composenews.remotedatasource.api

import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketResponse

interface MarketsApi {
    suspend fun getMarkets(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean,
    ): List<MarketResponse>

    suspend fun getMarketChart(
        id: String,
        currency: String,
        days: Int,
    ): MarketChartResponse

    suspend fun getMarketDetail(id: String): MarketDetailResponse
}
