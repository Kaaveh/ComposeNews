package ir.kaaveh.data.repository.mapper

import ir.kaaveh.data.mapper.toMarket
import ir.kaaveh.data.mapper.toRemoteNewsDto
import ir.kaaveh.domain.test.notFavoriteNews
import ir.kaaveh.localdatasource.test.remoteNewsDto
import ir.kaaveh.remotedatasource.test.marketDto
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsDtoMapperTest{

    @Test
    fun newsDtoToNews() {
        val mappedNews = marketDto.toMarket()
        assertEquals(mappedNews, notFavoriteNews)
    }

    @Test
    fun newsDtoToRemoteNewsDto() {
        val mappedRemoteNews = marketDto.toRemoteNewsDto()
        assertEquals(mappedRemoteNews, remoteNewsDto)
    }

}