package ir.kaaveh.remotedatasource.test

import ir.kaaveh.remotedatasource.api.NewsApi
import ir.kaaveh.remotedatasource.dto.NewsResponse

class FakeNewsApi : NewsApi {
    override suspend fun getNews(
        countryCode: String,
        pageNumber: Int,
        apiKey: String
    ): NewsResponse = NewsResponse(
        news = listOf(newsDto),
        null,
        null,
    )
}