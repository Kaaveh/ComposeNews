package ir.kaaveh.localdatasource.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ir.kaaveh.localdatasource.test.favoriteLocalNewsDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class NewsDaoTest {

    private lateinit var newsDao: NewsDao
    private lateinit var db: NewsDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        newsDao = db.newsDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun emptyTableAtDbInitialization() = runTest {
        val newsList = newsDao.getNews().first()
        assertTrue(newsList.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertNewsToDb() = runTest {
        newsDao.insertNews(favoriteLocalNewsDto)
        val newsList = newsDao.getNews().first()
        assertTrue(newsList.contains(favoriteLocalNewsDto))
    }

}