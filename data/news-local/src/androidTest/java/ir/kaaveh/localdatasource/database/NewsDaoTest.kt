package ir.kaaveh.localdatasource.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ir.kaaveh.localdatasource.dto.LocalNewsDto
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
    private val localNewsDto = LocalNewsDto(
        author = "",
        description = "",
        publishedAt = "",
        source = "",
        title = "",
        url = "",
        urlToImage = "",
        isFavorite = true,
    )

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
        val newsList = newsDao.getAllNews().first()
        assertTrue(newsList.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertNewsToDb() = runTest {
        newsDao.insertNews(localNewsDto)
        val newsList = newsDao.getAllNews().first()
        assertTrue(newsList.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun setFavoriteNewsThenCheckIsFavorite() = runTest {
        newsDao.insertNews(localNewsDto)
        assertTrue(newsDao.isFavoriteNews(title = localNewsDto.title, source = localNewsDto.source))
    }

}