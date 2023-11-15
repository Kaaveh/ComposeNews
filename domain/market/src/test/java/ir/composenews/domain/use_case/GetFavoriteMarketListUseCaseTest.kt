@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.flowOf

class GetFavoriteMarketListUseCaseTest : StringSpec({

    val marketRepository: MarketRepository = mockk(relaxed = true)
    lateinit var getFavoriteMarketListUseCase: GetFavoriteMarketListUseCase

    beforeSpec {
        getFavoriteMarketListUseCase = GetFavoriteMarketListUseCase(repository = marketRepository)
    }

    "Check get only favorite markets" {
        every { marketRepository.getFavoriteMarketList() } returns flowOf(emptyList())
        getFavoriteMarketListUseCase.invoke()
        verify(exactly = 1) {
            marketRepository.getFavoriteMarketList()
        }
    }
})
