package ir.kaaveh.remotedatasource.api

import ir.kaaveh.remotedatasource.dto.MarketResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketsApi {

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("sparkline") sparkline: Boolean
    ): List<MarketResponse>

}

internal const val BASE_URL = "https://api.coingecko.com/api/v3/"