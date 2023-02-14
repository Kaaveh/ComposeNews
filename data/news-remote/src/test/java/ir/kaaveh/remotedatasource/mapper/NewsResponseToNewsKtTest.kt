package ir.kaaveh.remotedatasource.mapper

import ir.kaaveh.domain.test.notFavoriteNews
import ir.kaaveh.remotedatasource.test.newsDto
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsResponseToNewsKtTest{

    @Test
    fun newsDtoToNews() {
        val mappedNews = newsDto.toNews()
        assertEquals(mappedNews, notFavoriteNews)
    }

}