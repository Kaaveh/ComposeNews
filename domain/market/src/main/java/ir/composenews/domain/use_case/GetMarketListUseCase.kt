@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketListUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    operator fun invoke(): Flow<List<Market>> {
        return repository.getMarketList()
    }
}
