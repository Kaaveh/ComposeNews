package ir.composenews.localdatasource.test

import ir.composenews.db.Markets
import ir.composenews.localdatasource.dto.RemoteMarketDto

val localMarketDto = Markets(
    id = "id",
    name = "name",
    symbol = "symbol",
    currentPrice = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = 0,
)

val favoriteLocalMarketDto = Markets(
    id = "id",
    name = "name",
    symbol = "symbol",
    currentPrice = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = 1,
)

val remoteMarketDto = RemoteMarketDto(
    id = "id",
    name = "name",
    symbol = "symbol",
    currentPrice = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png",
)
