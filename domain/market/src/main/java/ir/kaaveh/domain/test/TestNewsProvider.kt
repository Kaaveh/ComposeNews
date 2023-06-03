package ir.kaaveh.domain.test

import ir.kaaveh.domain.model.Market

val notFavoriteMarket = Market(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = false,
)

val favoriteMarket = Market(
    id = "id",
    name = "name",
    currentPrice = 100000.0,
    imageUrl = "some_shit_url.png",
    isFavorite = true,
)