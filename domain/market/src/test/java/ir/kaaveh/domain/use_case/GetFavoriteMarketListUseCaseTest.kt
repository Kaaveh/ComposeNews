package ir.kaaveh.domain.use_case

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ir.kaaveh.domain.repository.MarketRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetFavoriteMarketListUseCaseTest {

    private val marketRepository: MarketRepository = mockk(relaxed = true)
    private lateinit var getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase

    @Before
    fun provideRepository() {
        getFavoriteMarketListUseCase = GetFavoriteMarketListUseCase(repository = marketRepository)
    }

    @Test
    fun checkGetOnlyFavoriteNews() = runTest {
        every { marketRepository.getFavoriteMarketList() } returns flowOf(emptyList())
        getFavoriteMarketListUseCase.invoke()
        verify(exactly = 1) {
            marketRepository.getFavoriteMarketList()
        }
    }

}