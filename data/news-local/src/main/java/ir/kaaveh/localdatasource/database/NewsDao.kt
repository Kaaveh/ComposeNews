package ir.kaaveh.localdatasource.database

import androidx.room.*
import ir.kaaveh.localdatasource.dto.LocalNewsDto
import ir.kaaveh.localdatasource.dto.RemoteNewsDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getNews(): Flow<List<LocalNewsDto>>

    @Query("SELECT * FROM news WHERE isFavorite")
    fun getFavoriteNews(): Flow<List<LocalNewsDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: LocalNewsDto)

    @Upsert(entity = LocalNewsDto::class)
    suspend fun upsertNews(news: RemoteNewsDto)

}