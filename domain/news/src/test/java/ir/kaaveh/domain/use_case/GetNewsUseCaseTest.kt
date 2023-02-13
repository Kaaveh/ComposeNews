package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.model.News
import ir.kaaveh.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class GetNewsUseCaseTest {

    private lateinit var mockRepository: NewsRepository
    private lateinit var getNewsUseCase: GetNewsUseCase
    private val news = News(
        author = "",
        description = "",
        publishedAt = "",
        source = "",
        title = "",
        url = "",
        urlToImage = "",
        isFavorite = true,
    )
    private val notFavoriteNews = News(
        author = "",
        description = "",
        publishedAt = "",
        source = "",
        title = "",
        url = "",
        urlToImage = "",
        isFavorite = false,
    )

    @Before
    fun provideRepository() {
        mockRepository = mock {
            on { getNews() } doReturn flow {
                emit(listOf(news, notFavoriteNews))
            }
        }
        getNewsUseCase = GetNewsUseCase(repository = mockRepository)
    }

    @Test
    fun getNews() = runTest {
        val news = getNewsUseCase().first()
        assertTrue(news.size == 2)
    }

}