package ir.composenews.domain.use_case

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.domain.use_case.GetFavoriteMarketListUseCase
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