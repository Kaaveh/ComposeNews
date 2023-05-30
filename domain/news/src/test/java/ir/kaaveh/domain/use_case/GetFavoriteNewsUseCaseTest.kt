package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.repository.MarketsRepository
import ir.kaaveh.domain.test.favoriteNews
import ir.kaaveh.domain.test.notFavoriteNews
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

    private lateinit var mockRepository: MarketsRepository
    private lateinit var getFavoriteNewsUseCase: GetFavoriteMarketsUseCase

    @Before
    fun provideRepository(){
        mockRepository = mock {
            on { getNews() } doReturn flow {
                emit(listOf(favoriteNews, notFavoriteNews))
            }
        }
        getFavoriteNewsUseCase = GetFavoriteMarketsUseCase(repository = mockRepository)
    }

    @Test
    fun checkGetOnlyFavoriteNews() = runTest {
        val news = getFavoriteNewsUseCase().first()
        assertTrue(news.size == 1)
    }

}