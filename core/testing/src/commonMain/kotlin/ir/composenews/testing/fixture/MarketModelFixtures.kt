package ir.composenews.testing.fixture

import ir.composenews.uimarket.model.MarketModel

fun buildMarketModel(
    id: String = "btc",
    name: String = "Bitcoin",
    symbol: String = "BTC",
    isFavorite: Boolean = false,
) = MarketModel(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = 50000.0,
    priceChangePercentage24h = 2.5,
    imageUrl = "",
    isFavorite = isFavorite,
)
