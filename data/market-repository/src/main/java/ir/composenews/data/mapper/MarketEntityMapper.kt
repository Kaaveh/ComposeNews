package ir.composenews.data.mapper

import ir.composenews.db.MarketEntity
import ir.composenews.domain.model.Market
import ir.composenews.localdatasource.database.FALSE
import ir.composenews.localdatasource.database.TRUE

fun MarketEntity.toMarket(): Market = Market(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    imageUrl = imageUrl,
    isFavorite = isFavorite == TRUE,
)

fun Market.toMarketEntity(): MarketEntity = MarketEntity(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    imageUrl = imageUrl,
    isFavorite = if (isFavorite) TRUE else FALSE,
)
