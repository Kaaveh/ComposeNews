@file:Suppress("TopLevelPropertyNaming")

package ir.composenews.remotedatasource.api

import ir.composenews.remotedatasource.dto.MarketChartResponse
import ir.composenews.remotedatasource.dto.MarketResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarketsApi {

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean,
    ): List<MarketResponse>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String,
        @Query("days") days: Int,
    ): MarketChartResponse
}

internal const val BASE_URL = "https://api.coingecko.com/api/v3/"
