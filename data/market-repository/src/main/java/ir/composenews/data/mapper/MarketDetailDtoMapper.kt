package ir.composenews.data.mapper

import ir.composenews.domain.model.MarketDetail
import ir.composenews.remotedatasource.dto.MarketDetailResponse

fun MarketDetailResponse.toDetail(): MarketDetail = MarketDetail (
    id = id,
    name = name,
    marketData = marketData?.toMarketData(),
    marketCapRank = marketCapRank
)


fun MarketDetailResponse.MarketData.toMarketData(): MarketDetail.MarketData = MarketDetail.MarketData (
    high24h = high24h?.toHigh24(),
    low24h = low24h?.toLow24(),
    marketCap = marketCap?.toMarketCap(),
    marketCapRank = marketCapRank
)

fun MarketDetailResponse.MarketData.High24h.toHigh24(): MarketDetail.MarketData.High24h = MarketDetail.MarketData.High24h (
    usd = usd
)

fun MarketDetailResponse.MarketData.Low24h.toLow24(): MarketDetail.MarketData.Low24h = MarketDetail.MarketData.Low24h (
    usd = usd
)

fun MarketDetailResponse.MarketData.MarketCap.toMarketCap(): MarketDetail.MarketData.MarketCap = MarketDetail.MarketData.MarketCap (
    usd = usd
)