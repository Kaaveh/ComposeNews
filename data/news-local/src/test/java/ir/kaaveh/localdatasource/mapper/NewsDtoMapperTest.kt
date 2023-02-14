package ir.kaaveh.localdatasource.mapper

import ir.kaaveh.domain.test.notFavoriteNews
import ir.kaaveh.localdatasource.test.localNewsDto
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsDtoMapperTest {

    @Test
    fun localNewsDtoToNews() {
        val mappedNews = localNewsDto.toNews()
        assertEquals(mappedNews, notFavoriteNews)
    }

    @Test
    fun newsToLocalNewsDto() {
        val mappedLocalNews = notFavoriteNews.toLocalNewsDto()
        assertEquals(mappedLocalNews, localNewsDto)
    }
}