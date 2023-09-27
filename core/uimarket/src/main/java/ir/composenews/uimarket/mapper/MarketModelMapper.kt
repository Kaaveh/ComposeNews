package ir.composenews.uimarket.mapper

import ir.composenews.domain.model.Market
import ir.composenews.uimarket.model.MarketModel

fun MarketModel.toMarket() = Market(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
)

fun Market.toMarketModel() = MarketModel(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
)