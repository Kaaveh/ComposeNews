@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import javax.inject.Inject

class ToggleFavoriteMarketListUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    suspend operator fun invoke(news: Market) = repository.toggleFavoriteMarket(news)
}
