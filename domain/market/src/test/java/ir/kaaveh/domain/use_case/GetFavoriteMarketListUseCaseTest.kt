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
class GetFavoriteMarketListUseCaseTest {

    private lateinit var mockRepository: MarketRepository
    private lateinit var getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase

    @Before
    fun provideRepository(){
        mockRepository = mock {
            on { getMarketList() } doReturn flow {
                emit(listOf(favoriteMarket, notFavoriteMarket))
            }
        }
        getFavoriteMarketListUseCase = GetFavoriteMarketListUseCase(repository = mockRepository)
    }

    @Test
    fun checkGetOnlyFavoriteNews() = runTest {
        val news = getFavoriteMarketListUseCase().first()
        assertTrue(news.size == 1)
    }

}