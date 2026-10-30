@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetMarketListUseCaseTest : StringSpec({

    lateinit var repository: MarketRepository
    lateinit var useCase: GetMarketListUseCase

    beforeTest {
        repository = mockk(relaxed = true)
        useCase = GetMarketListUseCase(repository = repository)
    }

    "Given repository returns market list, When invoked, Then flow emits those markets without syncing" {
        val market = Market(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 2.5,
            imageUrl = "",
            isFavorite = false,
        )
        every { repository.getMarketList() } returns flowOf(listOf(market))

        useCase.invoke().first() shouldBe listOf(market)
        coVerify(exactly = 0) { repository.syncMarketList() }
    }
})
