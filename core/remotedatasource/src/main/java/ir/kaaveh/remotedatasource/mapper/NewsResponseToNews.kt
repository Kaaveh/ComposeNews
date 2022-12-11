package ir.kaaveh.remotedatasource.mapper

import ir.kaaveh.domain.model.News
import ir.kaaveh.remotedatasource.dto.NewsDto

fun NewsDto.toNews(): News = News(
    author = author ?: "",
    description = description ?: "",
    publishedAt = publishedAt ?: "",
    source = source?.name ?: "",
    title = title ?: "",
    url = url ?: "",
    urlToImage = urlToImage ?: "",
)