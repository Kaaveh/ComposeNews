package ir.composenews.data.mapper

import ir.composenews.db.Markets
import ir.composenews.domain.model.Market

fun Markets.toMarket(): Market = Market(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    imageUrl = imageUrl,
    isFavorite = isFavorite == 1L,
)

fun Market.toLocalMarketDto(): Markets = Markets(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    imageUrl = imageUrl,
    isFavorite = if (isFavorite) 1 else 0,
)
