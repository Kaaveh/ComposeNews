@file:Suppress("PackageNaming", "PackageName", "ktlint:standard:class-signature")

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.network.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetMarketDetailUseCaseTest : StringSpec({

    lateinit var repository: MarketRepository
    lateinit var useCase: GetMarketDetailUseCase

    beforeTest {
        repository = mockk(relaxed = true)
        useCase = GetMarketDetailUseCase(repository = repository)
    }

    "Given repository, When invoked with a market id, Then fetchDetail is called with that id" {
        val id = "bitcoin"
        every { repository.fetchDetail(id = id) } returns flowOf()

        useCase.invoke(id)

        verify(exactly = 1) { repository.fetchDetail(id = id) }
    }

    "Given repository returns market detail, When invoked, Then flow emits that detail" {
        val id = "bitcoin"
        val detail = MarketDetail(id = id, name = "Bitcoin", marketCapRank = 1, marketData = null)
        every { repository.fetchDetail(id = id) } returns flowOf(Resource.Success(detail))

        val result = useCase.invoke(id).first()

        result shouldBe Resource.Success(detail)
    }
})
