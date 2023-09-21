@file:Suppress("ImportOrdering")

package ir.composenews.data.repository.mapper

import ir.composenews.data.mapper.toLocalMarketDto
import ir.composenews.data.mapper.toMarket
import ir.composenews.domain.test.notFavoriteMarket
import ir.composenews.localdatasource.test.localMarketDto
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
