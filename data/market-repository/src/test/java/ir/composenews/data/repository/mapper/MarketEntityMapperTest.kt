@file:Suppress("ImportOrdering")

package ir.composenews.data.repository.mapper

import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toMarketEntity
import ir.composenews.domain.test.notFavoriteMarket
import ir.composenews.localdatasource.test.marketEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class MarketEntityMapperTest {

    @Test
    fun localMarketDtoToNews() {
        val mappedMarket = marketEntity.toMarket()
        assertEquals(mappedMarket, notFavoriteMarket)
    }

    @Test
    fun newsToLocalNewsDto() {
        val mappedLocalNews = notFavoriteMarket.toMarketEntity()
        assertEquals(mappedLocalNews, marketEntity)
    }
}
