@file:Suppress("ktlint")
package ir.composenews.data.mapper

import ir.composenews.domain.model.MarketDetail
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse.MarketData

fun MarketDetailResponse.toMarketDetail(): MarketDetail = MarketDetail(
    id = id ?: "",
    name = name ?: "",
    marketData = marketData?.toMarketData(),
    marketCapRank = marketCapRank ?: 0,
)

fun MarketData.toMarketData(): MarketDetail.MarketData = MarketDetail.MarketData(
    high24hUSD = high24h?.usd ?: 0.0,
    low24hUSD = low24h?.usd ?: 0.0,
    marketCapUSD = marketCap?.usd ?: 0,
    marketCapRank = marketCapRank ?: 0,
)
