@file:Suppress("PackageNaming", "PackageName", "ktlint:standard:class-signature")

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.network.Resource

class SyncMarketListUseCaseTest : StringSpec({

    lateinit var repository: MarketRepository
    lateinit var useCase: SyncMarketListUseCase

    beforeTest {
        repository = mockk(relaxed = true)
        useCase = SyncMarketListUseCase(repository = repository)
    }

    "Given repository, When invoked, Then syncMarketList is called exactly once" {
        coEvery { repository.syncMarketList() } returns Resource.Success(Unit)

        useCase.invoke()

        coVerify(exactly = 1) { repository.syncMarketList() }
    }
})
