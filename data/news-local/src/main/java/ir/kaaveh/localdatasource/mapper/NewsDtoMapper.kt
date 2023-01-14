package ir.kaaveh.localdatasource.mapper

import ir.kaaveh.domain.model.News
import ir.kaaveh.localdatasource.dto.NewsDto

fun NewsDto.toNews(): News = News(
    author = author,
    description = description,
    publishedAt = publishedAt,
    source = source,
    title = title,
    url = url,
    urlToImage = urlToImage,
    isFavorite = true,
)

fun News.toNewsDto(): NewsDto = NewsDto(
    title = title,
    author = author,
    description = description,
    publishedAt = publishedAt,
    source = source,
    url = url,
    urlToImage = urlToImage,
)
