package ir.kaaveh.domain.repository

import ir.kaaveh.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(): List<News>
    suspend fun insertFavoriteNews(news: News)
    suspend fun removeFavoriteNews(news: News)
    suspend fun isFavoriteNews(news: News): Boolean
    fun getAllFavoriteNews(): Flow<List<News>>
}