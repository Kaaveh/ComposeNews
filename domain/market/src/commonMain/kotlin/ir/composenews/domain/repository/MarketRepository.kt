package ir.composenews.domain.repository

import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun getMarketList(): Flow<List<Market>>

    fun getFavoriteMarketList(): Flow<List<Market>>

    fun getMarketById(id: String): Flow<Market?>

    suspend fun syncMarketList(): Resource<Unit, Errors>

    suspend fun toggleFavoriteMarket(oldMarket: Market)

    fun fetchChart(
        id: String,
        days: Int,
    ): Flow<Resource<MarketChart, Errors>>

    fun fetchDetail(id: String): Flow<Resource<MarketDetail, Errors>>
}
