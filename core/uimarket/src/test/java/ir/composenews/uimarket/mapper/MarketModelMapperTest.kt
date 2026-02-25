@file:Suppress("ktlint")

package ir.composenews.uimarket.mapper

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import ir.composenews.domain.model.Market
import ir.composenews.uimarket.model.MarketModel

class MarketModelMapperTest : StringSpec({

    "Given a domain Market, When mapped to MarketModel, Then all fields are correctly mapped" {
        val market = Market(
            id = "btc",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 2.5,
            imageUrl = "https://example.com/btc.png",
            isFavorite = false,
        )

        val model = market.toMarketModel()

        model.id shouldBe market.id
        model.name shouldBe market.name
        model.symbol shouldBe market.symbol
        model.currentPrice shouldBe market.currentPrice
        model.priceChangePercentage24h shouldBe market.priceChangePercentage24h
        model.imageUrl shouldBe market.imageUrl
        model.isFavorite shouldBe market.isFavorite
    }

    "Given a favorited Market, When mapped to MarketModel, Then isFavorite is true" {
        val market = Market(
            id = "eth",
            name = "Ethereum",
            symbol = "ETH",
            currentPrice = 3000.0,
            priceChangePercentage24h = -1.5,
            imageUrl = "",
            isFavorite = true,
        )

        val model = market.toMarketModel()

        model.isFavorite shouldBe true
    }

    "Given a MarketModel, When mapped to Market and back to MarketModel, Then all fields are preserved" {
        val original = MarketModel(
            id = "sol",
            name = "Solana",
            symbol = "SOL",
            currentPrice = 150.0,
            priceChangePercentage24h = 10.0,
            imageUrl = "https://example.com/sol.png",
            isFavorite = true,
        )

        val roundTripped = original.toMarket().toMarketModel()

        roundTripped shouldBe original
    }

    "Given a Market with zero price change, When mapped to MarketModel, Then priceChangePercentage24h is 0.0" {
        val market = Market(
            id = "usdc",
            name = "USD Coin",
            symbol = "USDC",
            currentPrice = 1.0,
            priceChangePercentage24h = 0.0,
            imageUrl = "",
            isFavorite = false,
        )

        val model = market.toMarketModel()

        model.priceChangePercentage24h shouldBe 0.0
    }
})
