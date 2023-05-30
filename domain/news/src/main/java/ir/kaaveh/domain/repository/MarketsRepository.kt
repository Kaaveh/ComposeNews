package ir.kaaveh.domain.repository

import ir.kaaveh.domain.model.Market
import kotlinx.coroutines.flow.Flow

interface MarketsRepository {
    fun getMarkets(): Flow<List<Market>>
    fun getFavoriteMarkets(): Flow<List<Market>>
    suspend fun syncMarkets(): Boolean
    suspend fun toggleFavoriteMarkets(oldMarket: Market)
}