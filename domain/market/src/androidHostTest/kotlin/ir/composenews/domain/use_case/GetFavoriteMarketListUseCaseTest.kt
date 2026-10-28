@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.first
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

    "Given repository returns favorite markets, When invoked, Then flow emits those markets" {
        val market = Market(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 2.5,
            imageUrl = "",
            isFavorite = true,
        )
        every { marketRepository.getFavoriteMarketList() } returns flowOf(listOf(market))

        val result = getFavoriteMarketListUseCase.invoke().first()

        result shouldBe listOf(market)
    }

    "Given repository returns empty list, When invoked, Then flow emits empty list" {
        every { marketRepository.getFavoriteMarketList() } returns flowOf(emptyList())

        val result = getFavoriteMarketListUseCase.invoke().first()

        result shouldBe emptyList()
    }
})
