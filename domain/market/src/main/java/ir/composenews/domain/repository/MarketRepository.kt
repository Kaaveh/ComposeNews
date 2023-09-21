package ir.composenews.domain.repository

import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun getMarketList(): Flow<List<Market>>
    fun getFavoriteMarketList(): Flow<List<Market>>
    suspend fun syncMarketList()
    suspend fun toggleFavoriteMarket(oldMarket: Market)
    fun fetchChart(id: String): Flow<Resource<Chart>>
}
