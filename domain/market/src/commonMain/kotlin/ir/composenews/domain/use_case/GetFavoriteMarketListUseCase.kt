@file:Suppress("PackageNaming", "PackageName", "ktlint")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMarketListUseCase(
    private val repository: MarketRepository,
) {
    operator fun invoke(): Flow<List<Market>> = repository.getFavoriteMarketList()
}
