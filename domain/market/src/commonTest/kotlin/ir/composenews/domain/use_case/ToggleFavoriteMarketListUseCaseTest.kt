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

class ToggleFavoriteMarketListUseCaseTest : StringSpec({

    lateinit var repository: FakeMarketRepository
    lateinit var useCase: ToggleFavoriteMarketListUseCase

    beforeTest {
        repository = FakeMarketRepository()
        useCase = ToggleFavoriteMarketListUseCase(repository = repository)
    }

    "Given a non-favorited market, When invoked, Then toggleFavoriteMarket is called with that market" {
        val market = Market(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 2.5,
            imageUrl = "",
            isFavorite = false,
        )

        useCase.invoke(market)

        repository.toggled shouldBe listOf(market)
    }

    "Given a favorited market, When invoked, Then toggleFavoriteMarket is called with the same market" {
        val market = Market(
            id = "eth",
            name = "Ethereum",
            symbol = "ETH",
            currentPrice = 3000.0,
            priceChangePercentage24h = -1.5,
            imageUrl = "",
            isFavorite = true,
        )

        useCase.invoke(market)

        repository.toggled shouldBe listOf(market)
    }
})
