@file:Suppress("PackageNaming", "PackageName", "ktlint:standard:annotation")

package ir.composenews.domain.use_case

import ir.composenews.domain.model.Market
import ir.composenews.domain.repository.MarketRepository
import javax.inject.Inject

open class ToggleFavoriteMarketListUseCase @Inject constructor(
    private val repository: MarketRepository,
) {
    open suspend operator fun invoke(news: Market) = repository.toggleFavoriteMarket(news)
}
