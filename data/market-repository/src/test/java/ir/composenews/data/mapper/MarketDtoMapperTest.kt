@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.data.mapper

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.localdatasource.database.MarketEntity
import ir.composenews.domain.model.Market
import ir.composenews.remotedatasource.dto.MarketResponse

class MarketDtoMapperTest : StringSpec({

    "Given valid market response, When converting to market entity, Then returns correct dto" {
        val marketResponse = MarketResponse(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 20000.0,
            priceChangePercentage24h = 2.0,
            imageUrl = "https://image.url/bitcoin.png",
        )

        val marketEntity = marketResponse.toMarketEntity()

        marketEntity shouldBeEqual marketResponse
    }

    "Given market response with empty strings and zeros, When converting to market entity, Then returns correct dto" {

        val marketResponse = MarketResponse(
            id = "",
            name = "",
            symbol = "",
            currentPrice = 0.0,
            priceChangePercentage24h = 0.0,
            imageUrl = "",
        )

        val marketEntity = marketResponse.toMarketEntity()

        marketEntity shouldBeEqual marketResponse
    }

    "Given market response with extreme double values, When converting to market entity, Then returns correct dto" {

        val marketResponse = MarketResponse(
            id = "extreme",
            name = "Extreme Market",
            symbol = "ext",
            currentPrice = Double.MAX_VALUE,
            priceChangePercentage24h = Double.MIN_VALUE,
            imageUrl = "https://image.url/extreme.png",
        )

        val marketEntity = marketResponse.toMarketEntity()

        marketEntity shouldBeEqual marketResponse
    }

    "Given market response with negative values, When converting to market entity, Then returns correct market with default isFavorite false" {
        val marketResponse = MarketResponse(
            id = "negative",
            name = "Negative Market",
            symbol = "neg",
            currentPrice = -100.0,
            priceChangePercentage24h = -5.0,
            imageUrl = "https://image.url/negative.png",
        )

        val marketEntity = marketResponse.toMarketEntity()

        marketEntity shouldBeEqual marketResponse
    }

    "Given valid market response, When converting to market, Then returns correct market with default isFavorite false" {
        val marketResponse = MarketResponse(
            id = "ethereum",
            name = "Ethereum",
            symbol = "eth",
            currentPrice = 1500.0,
            priceChangePercentage24h = -1.5,
            imageUrl = "https://image.url/ethereum.png",
        )

        val market = marketResponse.toMarket()

        market shouldBeEqual marketResponse
    }

    "Given market response with empty strings and zeros, When converting to market, Then returns correct market with default isFavorite false" {

        val marketResponse = MarketResponse(
            id = "",
            name = "",
            symbol = "",
            currentPrice = 0.0,
            priceChangePercentage24h = 0.0,
            imageUrl = "",
        )

        val market = marketResponse.toMarket()

        market shouldBeEqual marketResponse
    }

    "Given market response with extreme double values, When converting to market, Then returns correct market with default isFavorite false" {

        val marketResponse = MarketResponse(
            id = "extreme",
            name = "Extreme Market",
            symbol = "ext",
            currentPrice = Double.MAX_VALUE,
            priceChangePercentage24h = Double.MIN_VALUE,
            imageUrl = "https://image.url/extreme.png",
        )

        val market = marketResponse.toMarket()

        market shouldBeEqual marketResponse
    }

    "Given market response with negative values, When converting to market, Then returns correct market with default isFavorite false" {
        val marketResponse = MarketResponse(
            id = "negative",
            name = "Negative Market",
            symbol = "neg",
            currentPrice = -100.0,
            priceChangePercentage24h = -5.0,
            imageUrl = "https://image.url/negative.png",
        )

        val market = marketResponse.toMarket()

        market shouldBeEqual marketResponse
    }
})

private infix fun MarketEntity.shouldBeEqual(expected: MarketResponse) {
    id shouldBe expected.id
    name shouldBe expected.name
    symbol shouldBe expected.symbol
    currentPrice shouldBe expected.currentPrice
    priceChangePercentage24h shouldBe expected.priceChangePercentage24h
    imageUrl shouldBe expected.imageUrl
}

private infix fun Market.shouldBeEqual(expected: MarketResponse) {
    id shouldBe expected.id
    name shouldBe expected.name
    symbol shouldBe expected.symbol
    currentPrice shouldBe expected.currentPrice
    priceChangePercentage24h shouldBe expected.priceChangePercentage24h
    imageUrl shouldBe expected.imageUrl
    isFavorite shouldBe false
}
