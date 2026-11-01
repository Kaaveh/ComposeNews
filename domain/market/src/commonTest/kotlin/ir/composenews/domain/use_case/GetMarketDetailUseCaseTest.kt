@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.domain.model.MarketDetail
import ir.composenews.network.Resource
import ir.composenews.testing.repository.FakeMarketRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetMarketDetailUseCaseTest : StringSpec({

    lateinit var repository: FakeMarketRepository
    lateinit var useCase: GetMarketDetailUseCase

    beforeTest {
        repository = FakeMarketRepository()
        useCase = GetMarketDetailUseCase(repository = repository)
    }

    "Given repository, When invoked with a market id, Then fetchDetail is called with that id" {
        useCase.invoke("bitcoin")

        repository.detailCalls shouldBe listOf("bitcoin")
    }

    "Given repository returns market detail, When invoked, Then flow emits that detail" {
        val detail = MarketDetail(id = "bitcoin", name = "Bitcoin", marketCapRank = 1, marketData = null)
        repository.detail = { flowOf(Resource.Success(detail)) }

        useCase.invoke("bitcoin").first() shouldBe Resource.Success(detail)
    }
})
