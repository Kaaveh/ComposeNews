package ir.kaaveh.domain.repository

import ir.kaaveh.domain.model.Market
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun getMarketList(): Flow<List<Market>>
    fun getFavoriteMarketList(): Flow<List<Market>>
    suspend fun syncMarketList(): Boolean
    suspend fun toggleFavoriteMarket(oldMarket: Market)
}