package ir.composenews.localdatasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import ir.composenews.localdatasource.dto.LocalMarketDto
import ir.composenews.localdatasource.dto.RemoteMarketDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {

    @Query("SELECT * FROM market_list ORDER BY name ASC")
    fun getMarketList(): Flow<List<LocalMarketDto>>

    @Query("SELECT * FROM market_list WHERE isFavorite ORDER BY name ASC")
    fun getFavoriteMarketList(): Flow<List<LocalMarketDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketList(localMarketDto: LocalMarketDto)

    @Upsert(entity = LocalMarketDto::class)
    suspend fun upsertMarket(remoteMarketDto: List<RemoteMarketDto>)
}
