package ir.composenews.domain.repository

import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketDetail
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun getMarketList(): Flow<List<Market>>
    fun getFavoriteMarketList(): Flow<List<Market>>
    suspend fun syncMarketList()
    suspend fun toggleFavoriteMarket(oldMarket: Market)
    fun fetchChart(id: String): Flow<Resource<Chart, Errors>>
    fun fetchDetail(id: String): Flow<Resource<MarketDetail, Errors>>
}
