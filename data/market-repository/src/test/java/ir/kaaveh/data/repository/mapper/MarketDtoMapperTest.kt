package ir.kaaveh.data.repository.mapper

import ir.kaaveh.data.mapper.toMarket
import ir.kaaveh.data.mapper.toRemoteMarketDto
import ir.composenews.domain.test.notFavoriteMarket
import ir.composenews.localdatasource.test.remoteMarketDto
import ir.kaaveh.remotedatasource.test.marketDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MarketDtoMapperTest{

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