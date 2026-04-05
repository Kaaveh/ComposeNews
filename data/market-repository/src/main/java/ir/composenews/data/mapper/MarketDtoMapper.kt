@file:Suppress("ktlint")

package ir.composenews.data.mapper

import ir.composenews.db.MarketEntity
import ir.composenews.domain.model.Market
import ir.composenews.localdatasource.database.FALSE
import ir.composenews.remotedatasource.dto.MarketResponse

fun MarketResponse.toMarketEntity(): MarketEntity = MarketEntity(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice ?: 0.0,
    priceChangePercentage24h = priceChangePercentage24h ?: 0.0,
    imageUrl = imageUrl,
    isFavorite = FALSE,
)

fun MarketResponse.toMarket(): Market = Market(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice ?: 0.0,
    priceChangePercentage24h = priceChangePercentage24h ?: 0.0,
    imageUrl = imageUrl,
)
