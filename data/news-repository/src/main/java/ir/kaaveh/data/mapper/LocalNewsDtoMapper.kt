package ir.kaaveh.data.mapper

import ir.kaaveh.domain.model.News
import ir.kaaveh.localdatasource.dto.LocalNewsDto

fun LocalNewsDto.toNews(): News = News(
    author = author,
    description = description,
    publishedAt = publishedAt,
    source = source,
    title = title,
    url = url,
    urlToImage = urlToImage,
    isFavorite = isFavorite,
)

fun News.toLocalNewsDto(): LocalNewsDto = LocalNewsDto(
    title = title,
    author = author,
    description = description,
    publishedAt = publishedAt,
    source = source,
    url = url,
    urlToImage = urlToImage,
    isFavorite = isFavorite
)
