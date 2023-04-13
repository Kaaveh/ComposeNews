package ir.kaaveh.data.mapper

import ir.kaaveh.domain.model.News
import ir.kaaveh.localdatasource.dto.RemoteNewsDto
import ir.kaaveh.remotedatasource.dto.NewsDto

fun NewsDto.toRemoteNewsDto(): RemoteNewsDto = RemoteNewsDto(
    author = author.orEmpty(),
    description = description.orEmpty(),
    publishedAt = publishedAt.orEmpty(),
    source = source?.name.orEmpty(),
    title = title.orEmpty(),
    url = url.orEmpty(),
    urlToImage = urlToImage.orEmpty(),
)

fun NewsDto.toNews(): News = News(
    author = author.orEmpty(),
    description = description.orEmpty(),
    publishedAt = publishedAt.orEmpty(),
    source = source?.name.orEmpty(),
    title = title.orEmpty(),
    url = url.orEmpty(),
    urlToImage = urlToImage.orEmpty(),
)