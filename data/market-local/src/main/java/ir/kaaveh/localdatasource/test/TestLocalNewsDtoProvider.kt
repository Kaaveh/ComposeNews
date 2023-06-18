package ir.kaaveh.localdatasource.test

import ir.kaaveh.localdatasource.dto.LocalMarketDto
import ir.kaaveh.localdatasource.dto.RemoteMarketDto

val localMarketDto = LocalMarketDto(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = false,
)

val favoriteLocalMarketDto = LocalMarketDto(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = true,
)

val remoteMarketDto = RemoteMarketDto(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
)