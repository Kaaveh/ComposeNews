package ir.kaaveh.localdatasource.database

import androidx.room.*
import ir.kaaveh.localdatasource.dto.LocalMarketsDto
import ir.kaaveh.localdatasource.dto.RemoteMarketDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {

    @Query("SELECT * FROM markets")
    fun getMarketList(): Flow<List<LocalMarketsDto>>

    @Query("SELECT * FROM markets WHERE isFavorite")
    fun getFavoriteMarketList(): Flow<List<LocalMarketsDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketList(localMarketsDto: LocalMarketsDto)

    @Upsert(entity = LocalMarketsDto::class)
    suspend fun upsertMarket(remoteMarketDto: RemoteMarketDto)

}