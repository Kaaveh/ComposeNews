@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Ordering
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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

    "Given repository syncs market data, When invoked, Then syncMarketList is called" {
        coEvery { repository.syncMarketList() } just Runs

        useCase.invoke()

        coVerify { repository.syncMarketList() }
    }

    "Given repository returns market list, When invoked, Then flow emits those markets" {
        val market = Market(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 2.5,
            imageUrl = "",
            isFavorite = false,
        )
        coEvery { repository.syncMarketList() } just Runs
        every { repository.getMarketList() } returns flowOf(listOf(market))

        val result = useCase.invoke().first()

        result shouldBe listOf(market)
    }

    "Given invoked, When repository methods are called, Then syncMarketList is called before getMarketList" {
        coEvery { repository.syncMarketList() } just Runs
        every { repository.getMarketList() } returns flowOf(emptyList())

        useCase.invoke()

        coVerify(ordering = Ordering.SEQUENCE) {
            repository.syncMarketList()
            repository.getMarketList()
        }
    }
})
