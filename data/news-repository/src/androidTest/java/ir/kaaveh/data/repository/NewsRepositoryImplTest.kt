package ir.kaaveh.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ir.kaaveh.localdatasource.database.NewsDao
import ir.kaaveh.localdatasource.database.NewsDatabase
import ir.kaaveh.remotedatasource.test.FakeNewsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest {
    private lateinit var newsDao: NewsDao
    private lateinit var db: NewsDatabase
    private val api = FakeNewsApi()
    private lateinit var repositoryImpl: NewsRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        newsDao = db.newsDao
        repositoryImpl = NewsRepositoryImpl(api = api, dao = newsDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun initialDbEmptyState() = runTest {
        val news = repositoryImpl.getNews().first()
        assertTrue(news.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getNewsFromInternet_thenReadItFromDb() = runTest {
        val result = repositoryImpl.syncNews()
        assertTrue(result)

        val news = repositoryImpl.getNews().first()
        assertTrue(news.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun toggleFavoriteNewsState() = runTest {
        repositoryImpl.syncNews()

        val oldNews = repositoryImpl.getNews().first().first()
        assertFalse(oldNews.isFavorite)

        repositoryImpl.toggleFavoriteNews(oldNews)
        val news = repositoryImpl.getNews().first().first()
        assertTrue(news.isFavorite)
    }

    @Test
    @Throws(Exception::class)
    fun toggleFavoriteNewsState_thenVerifyItsState() = runTest {
        repositoryImpl.syncNews()

        val oldNews = repositoryImpl.getNews().first().first()

        repositoryImpl.toggleFavoriteNews(oldNews)
        val isFavorite = repositoryImpl.isFavoriteNews(oldNews)
        assertTrue(isFavorite)
    }

}