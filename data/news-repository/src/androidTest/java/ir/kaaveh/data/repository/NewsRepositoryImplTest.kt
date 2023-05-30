package ir.kaaveh.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ir.kaaveh.localdatasource.database.MarketDao
import ir.kaaveh.localdatasource.database.MarketsDatabase
import ir.kaaveh.remotedatasource.test.FakeMarketsApi
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
    private lateinit var newsDao: MarketDao
    private lateinit var db: MarketsDatabase
    private val api = FakeMarketsApi()
    private lateinit var repositoryImpl: MarketsRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MarketsDatabase::class.java).build()
        newsDao = db.newsDao
        repositoryImpl = MarketsRepositoryImpl(api = api, dao = newsDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun initialDbEmptyState() = runTest {
        val news = repositoryImpl.getMarkets().first()
        assertTrue(news.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getNewsFromInternet_thenReadItFromDb() = runTest {
        val result = repositoryImpl.syncMarkets()
        assertTrue(result)

        val news = repositoryImpl.getMarkets().first()
        assertTrue(news.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun toggleFavoriteNewsState() = runTest {
        repositoryImpl.syncMarkets()

        val oldNews = repositoryImpl.getMarkets().first().first()
        assertFalse(oldNews.isFavorite)

        repositoryImpl.toggleFavoriteMarkets(oldNews)
        val news = repositoryImpl.getMarkets().first().first()
        assertTrue(news.isFavorite)
    }

}