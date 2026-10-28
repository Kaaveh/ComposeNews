@file:Suppress("PackageNaming", "PackageName", "ktlint:standard:class-signature")

package ir.composenews.domain.use_case

import io.kotest.core.spec.style.StringSpec
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import ir.composenews.domain.repository.MarketRepository

class SyncMarketListUseCaseTest : StringSpec({

    lateinit var repository: MarketRepository
    lateinit var useCase: SyncMarketListUseCase

    beforeTest {
        repository = mockk(relaxed = true)
        useCase = SyncMarketListUseCase(repository = repository)
    }

    "Given repository, When invoked, Then syncMarketList is called exactly once" {
        coEvery { repository.syncMarketList() } just Runs

        useCase.invoke()

        coVerify(exactly = 1) { repository.syncMarketList() }
    }
})
