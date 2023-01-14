package ir.kaaveh.data.repository

import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.repository.NewsRepository
import ir.kaaveh.localdatasource.database.FavoriteNewsDao
import ir.kaaveh.localdatasource.mapper.toFavoriteNewsDto
import ir.kaaveh.localdatasource.mapper.toNews
import ir.kaaveh.localdatasource.mapper.toNewsDto
import ir.kaaveh.remotedatasource.api.NewsApi
import ir.kaaveh.remotedatasource.mapper.toNews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: FavoriteNewsDao,
) : NewsRepository {

    override fun getNews(): Flow<List<News>> =
        dao.getAllNews().map { list -> list.map { it.toNews() } }

    override suspend fun updateNews(): Boolean = try {
        println("xxxxxxxxxxxxxxxx work")
        api.getNews().news
            .map { it.toNews().toNewsDto() }
            .onEach {
                dao.insertNews(it)
            }
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun insertFavoriteNews(news: News) =
        dao.insertFavoriteNews(news = news.toFavoriteNewsDto())

    override suspend fun removeFavoriteNews(news: News) =
        dao.deleteFavoriteNews(news = news.toFavoriteNewsDto())

    override suspend fun isFavoriteNews(news: News): Boolean =
        dao.isFavoriteNews(
            title = news.title,
            source = news.source
        )

    override fun getAllFavoriteNews(): Flow<List<News>> =
        dao.getAllFavoriteNews().map { list -> list.map { it.toNews() } }

}