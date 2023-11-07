@file:Suppress("ImportOrdering")

package ir.composenews.data.repository.mapper

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import ir.composenews.data.mapper.toMarket
import ir.composenews.data.mapper.toMarketEntity
import ir.composenews.domain.test.notFavoriteMarket
import ir.composenews.localdatasource.test.marketEntity

class MarketEntityMapperTest : StringSpec({
    "Local market dto to market" {
        val mappedMarket = marketEntity.toMarket()
        mappedMarket shouldBeEqual notFavoriteMarket
    }
    "newsToLocalNewsDto" {
        val mappedMarketEntity = notFavoriteMarket.toMarketEntity()
        mappedMarketEntity shouldBeEqual marketEntity
    }
})