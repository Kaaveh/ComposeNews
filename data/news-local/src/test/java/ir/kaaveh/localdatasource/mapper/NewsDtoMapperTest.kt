package ir.kaaveh.localdatasource.mapper

import ir.kaaveh.domain.model.News
import ir.kaaveh.localdatasource.dto.LocalNewsDto
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsDtoMapperTest {

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

    private val localNewsDto = LocalNewsDto(
        title = "",
        author = "",
        description = "",
        publishedAt = "",
        source = "",
        url = "",
        urlToImage = "",
        isFavorite = false,
    )

    @Test
    fun localNewsDtoToNews() {
        val mappedNews = localNewsDto.toNews()
        assertEquals(mappedNews, news)
    }

    @Test
    fun newsToLocalNewsDto() {
        val mappedLocalNews = news.toLocalNewsDto()
        assertEquals(mappedLocalNews, localNewsDto)
    }
}