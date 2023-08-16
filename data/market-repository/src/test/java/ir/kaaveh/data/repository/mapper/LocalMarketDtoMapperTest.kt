package ir.kaaveh.data.repository.mapper

import ir.composenews.domain.test.notFavoriteMarket
import ir.kaaveh.data.mapper.toLocalMarketDto
import ir.kaaveh.data.mapper.toMarket
import ir.kaaveh.localdatasource.test.localMarketDto
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalMarketDtoMapperTest {

    @Test
    fun localMarketDtoToNews() {
        val mappedMarket = localMarketDto.toMarket()
        assertEquals(mappedMarket, notFavoriteMarket)
    }

    @Test
    fun newsToLocalNewsDto() {
        val mappedLocalNews = notFavoriteMarket.toLocalMarketDto()
        assertEquals(mappedLocalNews, localMarketDto)
    }
}