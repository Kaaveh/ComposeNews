package ir.composenews.core_test.fixture

import ir.composenews.uimarket.model.MarketModel

object MarketModelFixtures {
    fun buildMarketModel(): MarketModel = buildMarketModel(
        id = "btc",
        name = "Bitcoin",
        symbol = "BTC",
        currentPrice = 50000.0,
        priceChangePercentage24h = 2.5,
        imageUrl = "",
        isFavorite = false,
    )

    fun buildMarketModel(isFavorite: Boolean): MarketModel = buildMarketModel(
        id = "btc",
        name = "Bitcoin",
        symbol = "BTC",
        currentPrice = 50000.0,
        priceChangePercentage24h = 2.5,
        imageUrl = "",
        isFavorite = isFavorite,
    )

    fun buildMarketModel(id: String, name: String, symbol: String): MarketModel = buildMarketModel(
        id = id,
        name = name,
        symbol = symbol,
        currentPrice = 50000.0,
        priceChangePercentage24h = 2.5,
        imageUrl = "",
        isFavorite = false,
    )

    fun buildMarketModel(
        id: String,
        name: String,
        symbol: String,
        currentPrice: Double,
        priceChangePercentage24h: Double,
        imageUrl: String,
        isFavorite: Boolean,
    ): MarketModel = MarketModel(
        id = id,
        name = name,
        symbol = symbol,
        currentPrice = currentPrice,
        priceChangePercentage24h = priceChangePercentage24h,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
    )
}
