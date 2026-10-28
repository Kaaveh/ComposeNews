@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository

class ToggleFavoriteMarketListUseCaseTest : StringSpec({

    lateinit var repository: MarketRepository
    lateinit var useCase: ToggleFavoriteMarketListUseCase

    beforeTest {
        repository = mockk(relaxed = true)
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
        coEvery { repository.toggleFavoriteMarket(market) } just Runs

        useCase.invoke(market)

        coVerify(exactly = 1) { repository.toggleFavoriteMarket(market) }
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
        coEvery { repository.toggleFavoriteMarket(market) } just Runs

        useCase.invoke(market)

        coVerify(exactly = 1) { repository.toggleFavoriteMarket(market) }
    }
})
