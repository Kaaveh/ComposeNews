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
class GetFavoriteNewsUseCaseTest {

    private lateinit var mockRepository: NewsRepository
    private lateinit var getFavoriteNewsUseCase: GetFavoriteNewsUseCase
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
    fun provideRepository(){
        mockRepository = mock {
            on { getNews() } doReturn flow {
                emit(listOf(news, notFavoriteNews))
            }
        }
        getFavoriteNewsUseCase = GetFavoriteNewsUseCase(repository = mockRepository)
    }

    @Test
    fun checkGetOnlyFavoriteNews() = runTest {
        val news = getFavoriteNewsUseCase().first()
        assertTrue(news.size == 1)
    }

}