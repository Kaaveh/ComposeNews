package ir.kaaveh.data.repository

import ir.kaaveh.remotedatasource.api.NewsApi
import ir.kaaveh.remotedatasource.dto.NewsDto
import ir.kaaveh.remotedatasource.dto.NewsResponse

class FakeNewsApi : NewsApi {
    override suspend fun getNews(
        countryCode: String,
        pageNumber: Int,
        apiKey: String
    ): NewsResponse = NewsResponse(
        news = listOf(
            NewsDto(
                author = "",
                description = "",
                publishedAt = "",
                source = null,
                title = "",
                url = "",
                urlToImage = "",
            )
        ),
        null,
        null,
    )
}