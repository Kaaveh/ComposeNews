@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.domain.use_case

import ir.composenews.domain.repository.MarketRepository
import javax.inject.Inject

class SyncMarketListUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    suspend operator fun invoke() = repository.syncMarketList()
}
