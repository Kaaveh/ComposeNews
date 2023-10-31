package ir.composenews.localdatasource.database

import ir.composenews.db.MarketEntity
import ir.composenews.localdatasource.dto.RemoteMarketDto
import kotlinx.coroutines.flow.Flow

interface MarketDao {

    fun getMarketList(): Flow<List<MarketEntity>>

    fun getFavoriteMarketList(): Flow<List<MarketEntity>>

    suspend fun insertMarket(marketEntity: MarketEntity)

    suspend fun upsertMarket(remoteMarketDto: List<RemoteMarketDto>)
}
