package ir.kaaveh.localdatasource.mapper

import ir.kaaveh.domain.model.News
import ir.kaaveh.localdatasource.dto.FavoriteNewsDto

fun FavoriteNewsDto.toNews(): News = News(
    author = author,
    description = description,
    publishedAt = publishedAt,
    source = source,
    title = title,
    url = url,
    urlToImage = urlToImage,
    isFavorite = true,
)

fun News.toFavoriteNewsDto(): FavoriteNewsDto = FavoriteNewsDto(
    title = title,
    author = author,
    description = description,
    publishedAt = publishedAt,
    source = source,
    url = url,
    urlToImage = urlToImage,
)