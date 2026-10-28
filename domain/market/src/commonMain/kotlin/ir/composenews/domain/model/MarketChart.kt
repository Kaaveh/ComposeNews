package ir.composenews.domain.model

import kotlinx.collections.immutable.PersistentList

typealias PricePoint = Pair<Long, Double>

data class MarketChart(
    val prices: PersistentList<PricePoint>,
)
