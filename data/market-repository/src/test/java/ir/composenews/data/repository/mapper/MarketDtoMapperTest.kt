package ir.composenews.data.repository.mapper

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toRemoteMarketDto
import ir.composenews.domain.test.notFavoriteMarket
import ir.composenews.localdatasource.test.remoteMarketDto
import ir.composenews.remotedatasource.test.marketDto

class MarketDtoMapperTest : StringSpec({
    "marketDtoToMarket" {
        val mappedMarket = marketDto.toMarket()
        mappedMarket shouldBeEqual notFavoriteMarket
    }

    "marketDtoToRemoteMarketDto" {
        val mappedRemoteMarket = marketDto.toRemoteMarketDto()
        mappedRemoteMarket shouldBeEqual remoteMarketDto
    }
})
