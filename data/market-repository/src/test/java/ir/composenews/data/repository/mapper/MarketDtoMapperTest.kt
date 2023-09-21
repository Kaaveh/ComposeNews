package ir.composenews.data.repository.mapper

import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toRemoteMarketDto
import ir.composenews.domain.test.notFavoriteMarket
import ir.composenews.localdatasource.test.remoteMarketDto
import ir.composenews.remotedatasource.test.marketDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MarketDtoMapperTest {

    @Test
    fun marketDtoToMarket() {
        val mappedMarket = marketDto.toMarket()
        assertEquals(mappedMarket, notFavoriteMarket)
    }

    @Test
    fun marketDtoToRemoteMarketDto() {
        val mappedRemoteMarket = marketDto.toRemoteMarketDto()
        assertEquals(mappedRemoteMarket, remoteMarketDto)
    }
}
