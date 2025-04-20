@file:Suppress("PackageNaming", "PackageName", "ktlint")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow

class GetMarketListUseCase(
    private val repository: MarketRepository,
) {
    suspend operator fun invoke(): Flow<List<Market>> {
        repository.syncMarketList()
        return repository.getMarketList()
    }
}
