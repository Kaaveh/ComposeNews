@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.data.mapper

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.domain.model.MarketDetail
import ir.composenews.remotedatasource.dto.MarketDetailResponse

class MarketDetailDtoMapperKtTest : StringSpec({
    "Given valid market detail response, When converting to market detail, Then returns correct market detail" {
        val marketDetailResponse = MarketDetailResponse(
            id = "bitcoin",
            name = "Bitcoin",
            marketCapRank = 1,
            marketData = MarketDetailResponse.MarketData(
                high24h = MarketDetailResponse.MarketData.High24h(60000.0),
                low24h = MarketDetailResponse.MarketData.Low24h(55000.0),
                marketCap = MarketDetailResponse.MarketData.MarketCap(1000000000L),
                marketCapRank = 1,
            ),
        )

        val marketDetail = marketDetailResponse.toMarketDetail()

        marketDetail shouldBeEqual marketDetailResponse
    }

    "Given market detail response with null values, When converting to market detail, Then returns default values" {
        val marketDetailResponse = MarketDetailResponse(
            id = null,
            name = null,
            marketCapRank = null,
            marketData = null,
        )

        val marketDetail = marketDetailResponse.toMarketDetail()

        marketDetail shouldBeEqual marketDetailResponse
    }

    "Given market detail response with null market data, When converting to market detail, Then returns null market data" {
        val marketDetailResponse = MarketDetailResponse(
            id = "ethereum",
            name = "Ethereum",
            marketCapRank = 2,
            marketData = null,
        )

        val marketDetail = marketDetailResponse.toMarketDetail()

        marketDetail shouldBeEqual marketDetailResponse
    }

    "Given market data with null fields, When converting to market detail, Then assigns default values" {
        val marketData = MarketDetailResponse.MarketData(
            high24h = null,
            low24h = null,
            marketCap = null,
            marketCapRank = null,
        )

        val convertedMarketData = marketData.toMarketData()

        convertedMarketData shouldBeEqual marketData
    }

    "Given market data with negative values, When converting to market detail, Then preserves values" {
        val marketData = MarketDetailResponse.MarketData(
            high24h = MarketDetailResponse.MarketData.High24h(-60000.0),
            low24h = MarketDetailResponse.MarketData.Low24h(-55000.0),
            marketCap = MarketDetailResponse.MarketData.MarketCap(-1000000000L),
            marketCapRank = -1,
        )

        val convertedMarketData = marketData.toMarketData()

        convertedMarketData shouldBeEqual marketData
    }

    "Given market detail response with zero values, When converting to market detail, Then assigns zero values" {
        val marketDetailResponse = MarketDetailResponse(
            id = "dogecoin",
            name = "Dogecoin",
            marketCapRank = 0,
            marketData = MarketDetailResponse.MarketData(
                high24h = MarketDetailResponse.MarketData.High24h(0.0),
                low24h = MarketDetailResponse.MarketData.Low24h(0.0),
                marketCap = MarketDetailResponse.MarketData.MarketCap(0L),
                marketCapRank = 0,
            ),
        )

        val marketDetail = marketDetailResponse.toMarketDetail()

        marketDetail shouldBeEqual marketDetailResponse
    }
})

private infix fun MarketDetail.shouldBeEqual(expected: MarketDetailResponse) {
    id shouldBe (expected.id ?: "")
    name shouldBe (expected.name ?: "")
    marketCapRank shouldBe (expected.marketCapRank ?: 0)
    marketData?.shouldBeEqual(expected.marketData)
}

private infix fun MarketDetail.MarketData.shouldBeEqual(expected: MarketDetailResponse.MarketData?) {
    high24hUSD shouldBe (expected?.high24h?.usd ?: 0.0)
    low24hUSD shouldBe (expected?.low24h?.usd ?: 0.0)
    marketCapUSD shouldBe (expected?.marketCap?.usd ?: 0L)
    marketCapRank shouldBe (expected?.marketCapRank ?: 0)
}
