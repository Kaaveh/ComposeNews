package ir.kaaveh.domain.repository

import ir.kaaveh.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<News>>
    suspend fun updateNews(): Boolean
    suspend fun toggleFavoriteNews(news: News)
    suspend fun isFavoriteNews(news: News): Boolean
}