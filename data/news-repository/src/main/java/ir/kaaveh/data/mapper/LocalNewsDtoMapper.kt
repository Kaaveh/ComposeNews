package ir.kaaveh.data.mapper

import ir.kaaveh.domain.model.Market
import ir.kaaveh.localdatasource.dto.LocalMarketDto

fun LocalMarketDto.toNews(): Market = Market(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
)

fun Market.toLocalNewsDto(): LocalMarketDto = LocalMarketDto(
    id = id,
    name = name,
    currentPrice = currentPrice,
    imageUrl = imageUrl,
    isFavorite = isFavorite
)
