package ir.composenews.data.mapper

import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.model.MarketDetail.MarketData.High24h
import ir.composenews.domain.model.MarketDetail.MarketData.Low24h
import ir.composenews.domain.model.MarketDetail.MarketData.MarketCap
import ir.composenews.remotedatasource.dto.MarketDetailResponse
import ir.composenews.remotedatasource.dto.MarketDetailResponse.MarketData

fun MarketDetailResponse.toDetail(): MarketDetail = MarketDetail(
    id = id,
    name = name,
    marketData = marketData?.toMarketData(),
    marketCapRank = marketCapRank,
)

fun MarketData.toMarketData(): MarketDetail.MarketData = MarketDetail.MarketData(
    high24h = high24h?.toHigh24(),
    low24h = low24h?.toLow24(),
    marketCap = marketCap?.toMarketCap(),
    marketCapRank = marketCapRank,
)

fun MarketData.High24h.toHigh24(): High24h = High24h(
    usd = usd,
)

fun MarketData.Low24h.toLow24(): Low24h = Low24h(
    usd = usd,
)

fun MarketData.MarketCap.toMarketCap(): MarketCap = MarketCap(
    usd = usd,
)
