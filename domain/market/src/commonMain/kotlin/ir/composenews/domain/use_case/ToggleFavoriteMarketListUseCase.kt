@file:Suppress("PackageNaming", "PackageName", "ktlint:standard:annotation")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository

class ToggleFavoriteMarketListUseCase(
    private val repository: MarketRepository,
) {
    suspend operator fun invoke(market: Market) = repository.toggleFavoriteMarket(market)
}
