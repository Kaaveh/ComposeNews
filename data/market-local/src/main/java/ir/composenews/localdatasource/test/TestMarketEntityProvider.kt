package ir.composenews.localdatasource.test

import ir.composenews.db.MarketEntity
import ir.composenews.localdatasource.database.FALSE
import ir.composenews.localdatasource.database.TRUE
import ir.composenews.localdatasource.dto.RemoteMarketDto

val marketEntity = MarketEntity(
    id = "id",
    name = "name",
    symbol = "symbol",
    currentPrice = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = FALSE,
)

val favoriteMarketEntity = MarketEntity(
    id = "id",
    name = "name",
    symbol = "symbol",
    currentPrice = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = TRUE,
)

val remoteMarketDto = RemoteMarketDto(
    id = "id",
    name = "name",
    symbol = "symbol",
    currentPrice = 100000.0,
    priceChangePercentage24h = 100000.0,
    imageUrl = "some_shit_url.png",
)
