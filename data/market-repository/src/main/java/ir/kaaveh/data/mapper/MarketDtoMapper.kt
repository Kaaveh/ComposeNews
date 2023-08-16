package ir.kaaveh.data.mapper

import ir.composenews.domain.model.Market
import ir.kaaveh.localdatasource.dto.RemoteMarketDto
import ir.kaaveh.remotedatasource.dto.MarketResponse

fun MarketResponse.toRemoteMarketDto(): RemoteMarketDto = RemoteMarketDto(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl
)

fun MarketResponse.toMarket(): Market = Market(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl
)