package ir.kaaveh.localdatasource.database

import androidx.room.*
import ir.kaaveh.localdatasource.dto.LocalMarketDto
import ir.kaaveh.localdatasource.dto.RemoteMarketDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {

    @Query("SELECT * FROM market_list")
    fun getMarketList(): Flow<List<LocalMarketDto>>

    @Query("SELECT * FROM market_list WHERE isFavorite")
    fun getFavoriteMarketList(): Flow<List<LocalMarketDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketList(localMarketDto: LocalMarketDto)

    @Upsert(entity = LocalMarketDto::class)
    suspend fun upsertMarket(remoteMarketDto: RemoteMarketDto)

}