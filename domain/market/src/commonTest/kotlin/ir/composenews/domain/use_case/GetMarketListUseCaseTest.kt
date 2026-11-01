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

class GetMarketListUseCaseTest : StringSpec({

    lateinit var repository: FakeMarketRepository
    lateinit var useCase: GetMarketListUseCase

    beforeTest {
        repository = FakeMarketRepository()
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
        repository.marketList = { flowOf(listOf(market)) }

        useCase.invoke().first() shouldBe listOf(market)
        repository.syncCalls shouldBe 0
    }
})
