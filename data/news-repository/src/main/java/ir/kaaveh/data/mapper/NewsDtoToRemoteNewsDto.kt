package ir.kaaveh.data.mapper

import ir.kaaveh.localdatasource.dto.RemoteNewsDto
import ir.kaaveh.remotedatasource.dto.NewsDto

fun NewsDto.toRemoteNewsDto(): RemoteNewsDto = RemoteNewsDto(
    author = author ?: "",
    description = description ?: "",
    publishedAt = publishedAt ?: "",
    source = source?.name ?: "",
    title = title ?: "",
    url = url ?: "",
    urlToImage = urlToImage ?: "",
)