@file:Suppress("ktlint")

package ir.composenews.core.ui

import ir.composenews.domain.model.Market

private const val MARKET_LIST_COUNT = 10

val marketList = List(MARKET_LIST_COUNT) { index ->
    market().copy(
        name = "name $index",
    )
}

private fun market() = Market(
    id = "id",
    name = "name 1",
    symbol = "symbol",
    currentPrice = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png",
)
