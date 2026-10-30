@file:Suppress("ktlint")

package ir.composenews.data.mapper

import ir.composenews.localdatasource.database.MarketEntity
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
    marketCapRank = marketCapRank,
    totalVolume = totalVolume,
    sparkline7d = sparkline7d.toSparkline(),
)

fun Market.toMarketEntity(): MarketEntity = MarketEntity(
    id = id,
    name = name,
    symbol = symbol,
    currentPrice = currentPrice,
    priceChangePercentage24h = priceChangePercentage24h,
    imageUrl = imageUrl,
    isFavorite = if (isFavorite) TRUE else FALSE,
    marketCapRank = marketCapRank,
    totalVolume = totalVolume,
    sparkline7d = sparkline7d.takeIf { it.isNotEmpty() }?.toSparklineColumn(),
)

internal fun List<Double>.toSparklineColumn(): String = joinToString(",")

internal fun String?.toSparkline(): List<Double> =
    if (isNullOrEmpty()) emptyList() else split(',').map { it.toDouble() }
