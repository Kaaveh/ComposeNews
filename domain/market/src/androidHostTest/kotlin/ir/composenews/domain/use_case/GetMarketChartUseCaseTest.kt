@file:Suppress("PackageNaming", "PackageName", "ktlint:standard:class-signature")

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.network.Resource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetMarketChartUseCaseTest : StringSpec({

    lateinit var repository: MarketRepository
    lateinit var useCase: GetMarketChartUseCase

    beforeTest {
        repository = mockk(relaxed = true)
        useCase = GetMarketChartUseCase(repository = repository)
    }

    "Given repository, When invoked with a market id, Then fetchChart is called with that id" {
        val id = "bitcoin"
        every { repository.fetchChart(id = id) } returns flowOf()

        useCase.invoke(id)

        verify(exactly = 1) { repository.fetchChart(id = id) }
    }

    "Given repository returns chart data, When invoked, Then flow emits that chart" {
        val id = "bitcoin"
        val chart = MarketChart(prices = persistentListOf(Pair(1000L, 50000.0)))
        every { repository.fetchChart(id = id) } returns flowOf(Resource.Success(chart))

        val result = useCase.invoke(id).first()

        result shouldBe Resource.Success(chart)
    }
})
