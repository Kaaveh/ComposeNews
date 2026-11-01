@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.domain.model.MarketChart
import ir.composenews.network.Resource
import ir.composenews.testing.repository.FakeMarketRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetMarketChartUseCaseTest : StringSpec({

    lateinit var repository: FakeMarketRepository
    lateinit var useCase: GetMarketChartUseCase

    beforeTest {
        repository = FakeMarketRepository()
        useCase = GetMarketChartUseCase(repository = repository)
    }

    "Given repository, When invoked with a market id, Then fetchChart is called with that id and range" {
        useCase.invoke("bitcoin", days = 7)

        repository.chartCalls shouldBe listOf("bitcoin" to 7)
    }

    "Given repository returns chart data, When invoked, Then flow emits that chart" {
        val chart = MarketChart(prices = persistentListOf(Pair(1000L, 50000.0)))
        repository.chart = { _, _ -> flowOf(Resource.Success(chart)) }

        useCase.invoke("bitcoin", days = 7).first() shouldBe Resource.Success(chart)
    }
})
