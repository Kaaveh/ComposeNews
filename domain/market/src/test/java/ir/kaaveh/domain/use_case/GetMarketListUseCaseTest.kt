package ir.kaaveh.domain.use_case

import ir.kaaveh.domain.repository.MarketRepository
import ir.kaaveh.domain.test.favoriteMarket
import ir.kaaveh.domain.test.notFavoriteMarket
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
class GetMarketListUseCaseTest {

    private lateinit var mockRepository: MarketRepository
    private lateinit var getMarketListUseCase: GetMarketListUseCase

    @Before
    fun provideRepository() {
        mockRepository = mock {
            on { getMarketList() } doReturn flow {
                emit(listOf(favoriteMarket, notFavoriteMarket))
            }
        }
        getMarketListUseCase = GetMarketListUseCase(repository = mockRepository)
    }

    @Test
    fun getNews() = runTest {
        val news = getMarketListUseCase().first()
        assertTrue(news.size == 2)
    }

}