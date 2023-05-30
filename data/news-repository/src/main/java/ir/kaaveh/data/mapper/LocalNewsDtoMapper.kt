package ir.kaaveh.data.mapper

import ir.kaaveh.domain.model.Market
import ir.kaaveh.localdatasource.dto.LocalMarketsDto

fun LocalMarketsDto.toNews(): Market = Market(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
)

fun Market.toLocalNewsDto(): LocalMarketsDto = LocalMarketsDto(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl,
    isFavorite = isFavorite
)
