package ir.kaaveh.localdatasource.database

import androidx.room.*
import ir.kaaveh.localdatasource.dto.FavoriteNewsDto
import ir.kaaveh.localdatasource.dto.NewsDto
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteNewsDao {

    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsDto>>

    @Query("SELECT * FROM favorites")
    fun getAllFavoriteNews(): Flow<List<FavoriteNewsDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteNews(news: FavoriteNewsDto)

    @Delete
    suspend fun deleteFavoriteNews(news: FavoriteNewsDto)

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE title = :title AND source = :source)")
    suspend fun isFavoriteNews(title: String, source: String): Boolean

}