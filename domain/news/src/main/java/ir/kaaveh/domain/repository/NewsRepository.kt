package ir.kaaveh.domain.repository

import ir.kaaveh.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<News>>
    fun getFavoriteNews(): Flow<List<News>>
    suspend fun syncNews(): Boolean
    suspend fun toggleFavoriteNews(oldNews: News)
}