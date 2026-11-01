@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.domain.model.Market
import ir.composenews.testing.repository.FakeMarketRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetFavoriteMarketListUseCaseTest : StringSpec({

    lateinit var repository: FakeMarketRepository
    lateinit var useCase: GetFavoriteMarketListUseCase

    beforeTest {
        repository = FakeMarketRepository()
        useCase = GetFavoriteMarketListUseCase(repository = repository)
    }

    "Check get only favorite markets" {
        useCase.invoke()

        repository.favoriteListCalls shouldBe 1
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
        repository.favoriteList = { flowOf(listOf(market)) }

        useCase.invoke().first() shouldBe listOf(market)
    }

    "Given repository returns empty list, When invoked, Then flow emits empty list" {
        useCase.invoke().first() shouldBe emptyList()
    }
})
