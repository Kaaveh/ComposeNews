package ir.kaaveh.remotedatasource.mapper

import ir.kaaveh.domain.model.News
import ir.kaaveh.remotedatasource.dto.NewsDto
import org.junit.Assert.*
import org.junit.Test

class NewsResponseToNewsKtTest{

    private val news = News(
        author = "",
        description = "",
        publishedAt = "",
        source = "",
        title = "",
        url = "",
        urlToImage = "",
        isFavorite = false,
    )

    private val newsDto = NewsDto(
        author = "",
        description = "",
        publishedAt = "",
        source = null,
        title = "",
        url = "",
        urlToImage = "",
    )

    @Test
    fun newsDtoToNews() {
        val mappedNews = newsDto.toNews()
        assertEquals(mappedNews, news)
    }

}