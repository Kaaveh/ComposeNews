package ir.composenews.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ir.composenews.localdatasource.database.MarketDao
import ir.composenews.localdatasource.database.MarketsDatabase
import ir.composenews.remotedatasource.test.FakeMarketsApi
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
    private lateinit var marketDao: MarketDao
    private lateinit var db: MarketsDatabase
    private val api = FakeMarketsApi()
    private lateinit var repositoryImpl: MarketRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MarketsDatabase::class.java).build()
        marketDao = db.marketDao
        repositoryImpl = MarketRepositoryImpl(api = api, dao = marketDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun initialDbEmptyState() = runTest {
        val news = repositoryImpl.getMarketList().first()
        assertTrue(news.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getNewsFromInternet_thenReadItFromDb() = runTest {
        repositoryImpl.syncMarketList()

        val news = repositoryImpl.getMarketList().first()
        assertTrue(news.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun toggleFavoriteNewsState() = runTest {
        repositoryImpl.syncMarketList()

        val oldNews = repositoryImpl.getMarketList().first().first()
        assertFalse(oldNews.isFavorite)

        repositoryImpl.toggleFavoriteMarket(oldNews)
        val news = repositoryImpl.getMarketList().first().first()
        assertTrue(news.isFavorite)
    }
}
