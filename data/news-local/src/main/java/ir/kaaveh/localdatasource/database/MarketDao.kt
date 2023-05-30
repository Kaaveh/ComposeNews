package ir.kaaveh.localdatasource.database

import androidx.room.*
import ir.kaaveh.localdatasource.dto.LocalMarketsDto
import ir.kaaveh.localdatasource.dto.RemoteMarketDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketDao {

    @Query("SELECT * FROM markets")
    fun getNews(): Flow<List<LocalMarketsDto>>

    @Query("SELECT * FROM markets WHERE isFavorite")
    fun getFavoriteNews(): Flow<List<LocalMarketsDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: LocalMarketsDto)

    @Upsert(entity = LocalMarketsDto::class)
    suspend fun upsertNews(news: RemoteMarketDto)

}