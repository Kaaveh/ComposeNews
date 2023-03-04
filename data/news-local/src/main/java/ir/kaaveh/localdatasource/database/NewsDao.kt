package ir.kaaveh.localdatasource.database

import androidx.room.*
import ir.kaaveh.localdatasource.dto.LocalNewsDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getNews(): Flow<List<LocalNewsDto>>

    @Query("SELECT * FROM news WHERE isFavorite")
    fun getFavoriteNews(): Flow<List<LocalNewsDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: LocalNewsDto)

    @Query("SELECT EXISTS(SELECT * FROM news WHERE title = :title AND source = :source AND isFavorite)")
    suspend fun isFavoriteNews(title: String, source: String): Boolean

}