@file:Suppress(
    "ktlint:standard:multiline-expression-wrapping",
    "ktlint:standard:function-signature",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.uimarket.mapper

import ir.composenews.domain.model.Market
import ir.composenews.uimarket.model.MarketModel

fun MarketModel.toMarket() = Market(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
)

fun Market.toMarketModel() = MarketModel(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
)
