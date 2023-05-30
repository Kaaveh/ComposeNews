package ir.kaaveh.localdatasource.test

import ir.kaaveh.localdatasource.dto.LocalMarketsDto
import ir.kaaveh.localdatasource.dto.RemoteMarketDto

val localNewsDto = LocalMarketsDto(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = false,
)

val favoriteLocalNewsDto = LocalMarketsDto(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = true,
)

val remoteNewsDto = RemoteMarketDto(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
)