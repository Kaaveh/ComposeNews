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
    sparkline7d = sparkline7d.toSparklineColumn(),
)

/** No points are stored as NULL, never as an empty string. */
internal fun List<Double>.toSparklineColumn(): String? = if (isEmpty()) null else joinToString(",")

/** Skips values that don't parse, so one bad row can't break the whole list. */
internal fun String?.toSparkline(): List<Double> =
    if (isNullOrEmpty()) emptyList() else split(',').mapNotNull { it.toDoubleOrNull() }
