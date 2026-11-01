@file:Suppress(
    "PackageNaming",
    "PackageName",
    "ktlint:standard:class-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.testing.repository.FakeMarketRepository

class SyncMarketListUseCaseTest : StringSpec({

    lateinit var repository: FakeMarketRepository
    lateinit var useCase: SyncMarketListUseCase

    beforeTest {
        repository = FakeMarketRepository()
        useCase = SyncMarketListUseCase(repository = repository)
    }

    "Given repository, When invoked, Then syncMarketList is called exactly once" {
        useCase.invoke()

        repository.syncCalls shouldBe 1
    }
})
