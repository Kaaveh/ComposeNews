package ir.composenews.localdatasource.database

import ir.composenews.db.Markets
import ir.composenews.localdatasource.dto.RemoteMarketDto
import kotlinx.coroutines.flow.Flow

interface MarketDao {

    fun getMarketList(): Flow<List<Markets>>

    fun getFavoriteMarketList(): Flow<List<Markets>>

    suspend fun insertMarket(localMarketDto: Markets)

    suspend fun upsertMarket(remoteMarketDto: List<RemoteMarketDto>)
}
