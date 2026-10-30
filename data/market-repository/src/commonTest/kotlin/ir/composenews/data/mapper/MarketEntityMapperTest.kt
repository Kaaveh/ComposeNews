@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.data.mapper

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.localdatasource.database.MarketEntity
import ir.composenews.domain.model.Market
import ir.composenews.localdatasource.database.FALSE
import ir.composenews.localdatasource.database.TRUE

class MarketEntityMapperTest : StringSpec({
    "Given market with a sparkline, When converting to local market and back, Then it is the same market" {
        val market = Market(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 67412.8,
            priceChangePercentage24h = 2.14,
            imageUrl = "",
            isFavorite = true,
            marketCapRank = 1,
            totalVolume = 3.5e10,
            sparkline7d = listOf(65880.12, 1.0E-5, 68105.4),
        )

        market.toMarketEntity().toMarket() shouldBe market
    }

    "Given market without a sparkline, When converting to local market, Then sparkline is stored as null" {
        Market("bitcoin", "Bitcoin", "btc", 1.0, 1.0, "", false).toMarketEntity().sparkline7d shouldBe null
    }

    "Given local market with a malformed sparkline, When converting to market model, Then bad values are skipped" {
        val entity = MarketEntity("bitcoin", "Bitcoin", "btc", 1.0, 1.0, "", FALSE, sparkline7d = "1.5,,abc,2.5,")

        entity.toMarket().sparkline7d shouldBe listOf(1.5, 2.5)
    }

    "Given local market from before version 2, When converting to market model, Then rank is null and sparkline empty" {
        val market = MarketEntity("bitcoin", "Bitcoin", "btc", 1.0, 1.0, "", FALSE).toMarket()

        market.marketCapRank shouldBe null
        market.sparkline7d shouldBe emptyList()
    }

    "Given valid local market, When converting to market model, Then returns correct model" {
        val marketEntity = MarketEntity(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 20000.0,
            priceChangePercentage24h = 2.0,
            imageUrl = "https://image.url/bitcoin.png",
            isFavorite = FALSE,
        )

        val market = marketEntity.toMarket()

        market shouldBeEqual marketEntity
    }

    "Given valid local market with true and false values, When converting to market model, Then returns correct model" {
        val marketEntityWithIsFavoriteFalse = MarketEntity(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 20000.0,
            priceChangePercentage24h = 2.0,
            imageUrl = "https://image.url/bitcoin.png",
            isFavorite = FALSE,
        )

        val marketWithIsFavoriteFalse = marketEntityWithIsFavoriteFalse.toMarket()

        marketWithIsFavoriteFalse shouldBeEqual marketEntityWithIsFavoriteFalse

        val marketEntityWithIsFavoriteTrue = MarketEntity(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 20000.0,
            priceChangePercentage24h = 2.0,
            imageUrl = "https://image.url/bitcoin.png",
            isFavorite = TRUE,
        )

        val marketWithIsFavoriteTrue = marketEntityWithIsFavoriteTrue.toMarket()

        marketWithIsFavoriteTrue shouldBeEqual marketEntityWithIsFavoriteTrue
    }

    "Given local market with empty strings and zeros, When converting to market model, Then returns correct model" {
        val marketEntity = MarketEntity(
            id = "",
            name = "",
            symbol = "",
            currentPrice = 0.0,
            priceChangePercentage24h = 0.0,
            imageUrl = "",
            isFavorite = FALSE,
        )

        val market = marketEntity.toMarket()

        market shouldBeEqual marketEntity
    }

    "Given local market with extreme double values, When converting to market model, Then returns correct model" {
        val marketEntity = MarketEntity(
            id = "extreme",
            name = "Extreme Market",
            symbol = "ext",
            currentPrice = Double.MAX_VALUE,
            priceChangePercentage24h = Double.MIN_VALUE,
            imageUrl = "https://image.url/extreme.png",
            isFavorite = FALSE,
        )

        val market = marketEntity.toMarket()

        market shouldBeEqual marketEntity
    }

    "Given local market with negative values, When converting to market model, Then returns correct market with default isFavorite false" {

        val marketEntity = MarketEntity(
            id = "negative",
            name = "Negative Market",
            symbol = "neg",
            currentPrice = -100.0,
            priceChangePercentage24h = -5.0,
            imageUrl = "https://image.url/negative.png",
            isFavorite = FALSE,
        )

        val market = marketEntity.toMarket()

        market shouldBeEqual marketEntity
    }

    "Given valid market model, When converting to local market, Then returns correct dto with default isFavorite false" {
        val market = Market(
            id = "ethereum",
            name = "Ethereum",
            symbol = "eth",
            currentPrice = 1500.0,
            priceChangePercentage24h = -1.5,
            imageUrl = "https://image.url/ethereum.png",
            isFavorite = true,
        )

        val marketEntity = market.toMarketEntity()

        marketEntity shouldBeEqual market
    }

    "Given valid market model with true and false values, When converting to local market, Then returns correct dto" {
        val marketWithIsFavoriteFalse = Market(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 20000.0,
            priceChangePercentage24h = 2.0,
            imageUrl = "https://image.url/bitcoin.png",
            isFavorite = false,
        )

        val marketEntityWithIsFavoriteFalse = marketWithIsFavoriteFalse.toMarketEntity()

        marketEntityWithIsFavoriteFalse shouldBeEqual marketWithIsFavoriteFalse

        val marketWithIsFavoriteTrue = Market(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "btc",
            currentPrice = 20000.0,
            priceChangePercentage24h = 2.0,
            imageUrl = "https://image.url/bitcoin.png",
            isFavorite = true,
        )

        val marketEntityWithIsFavoriteTrue = marketWithIsFavoriteTrue.toMarketEntity()

        marketEntityWithIsFavoriteTrue shouldBeEqual marketWithIsFavoriteTrue
    }

    "Given market model with empty strings and zeros, When converting to local market, Then returns correct dto with default isFavorite false" {

        val market = Market(
            id = "",
            name = "",
            symbol = "",
            currentPrice = 0.0,
            priceChangePercentage24h = 0.0,
            imageUrl = "",
        )

        val marketEntity = market.toMarketEntity()

        marketEntity shouldBeEqual market
    }

    "Given market model with extreme double values, When converting to local market, Then returns correct dto with default isFavorite false" {

        val market = Market(
            id = "extreme",
            name = "Extreme Market",
            symbol = "ext",
            currentPrice = Double.MAX_VALUE,
            priceChangePercentage24h = Double.MIN_VALUE,
            imageUrl = "https://image.url/extreme.png",
        )

        val marketEntity = market.toMarketEntity()

        marketEntity shouldBeEqual market
    }

    "Given market model with negative values, When converting to local market, Then returns correct dto with default isFavorite false" {
        val market = Market(
            id = "negative",
            name = "Negative Market",
            symbol = "neg",
            currentPrice = -100.0,
            priceChangePercentage24h = -5.0,
            imageUrl = "https://image.url/negative.png",
        )

        val marketEntity = market.toMarketEntity()

        marketEntity shouldBeEqual market
    }
})

private infix fun Market.shouldBeEqual(expected: MarketEntity) {
    id shouldBe expected.id
    name shouldBe expected.name
    symbol shouldBe expected.symbol
    currentPrice shouldBe expected.currentPrice
    priceChangePercentage24h shouldBe expected.priceChangePercentage24h
    imageUrl shouldBe expected.imageUrl
    isFavorite shouldBe (expected.isFavorite == TRUE)
}

private infix fun MarketEntity.shouldBeEqual(expected: Market) {
    id shouldBe expected.id
    name shouldBe expected.name
    symbol shouldBe expected.symbol
    currentPrice shouldBe expected.currentPrice
    priceChangePercentage24h shouldBe expected.priceChangePercentage24h
    imageUrl shouldBe expected.imageUrl
    isFavorite shouldBe if (expected.isFavorite) TRUE else FALSE
}
