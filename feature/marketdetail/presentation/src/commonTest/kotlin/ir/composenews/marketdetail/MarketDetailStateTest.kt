@file:Suppress("MaxLineLength", "ktlint")

package ir.composenews.marketdetail

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ir.composenews.base.LoadableData
import ir.composenews.base.isLoading
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf

class MarketDetailStateTest : StringSpec({

    "Given default state, Then market, marketChart and marketDetail are all Initial" {
        val state = MarketDetailContract.State()

        state.market.shouldBeInstanceOf<LoadableData.Initial>()
        state.marketChart.shouldBeInstanceOf<LoadableData.Initial>()
        state.marketDetail.shouldBeInstanceOf<LoadableData.Initial>()
    }

    "Given state where market is Loaded but marketChart is Loading, Then partial loading is represented correctly" {
        val marketModel = MarketModel(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 5.0,
            imageUrl = "",
            isFavorite = false,
        )
        val state = MarketDetailContract.State(
            market = LoadableData.Loaded(marketModel),
            marketChart = LoadableData.Loading,
        )

        state.market.shouldBeInstanceOf<LoadableData.Loaded<MarketModel>>()
        state.marketChart.isLoading shouldBe true
        state.marketDetail.shouldBeInstanceOf<LoadableData.Initial>()
    }

    "Given state where all three fields are Loaded, Then no loading indicators are present" {
        val marketModel = MarketModel(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            currentPrice = 50000.0,
            priceChangePercentage24h = 5.0,
            imageUrl = "",
            isFavorite = false,
        )
        val state = MarketDetailContract.State(
            market = LoadableData.Loaded(marketModel),
            marketChart = LoadableData.Loaded(MarketChart(prices = persistentListOf())),
            marketDetail = LoadableData.Loaded(
                MarketDetail(id = "1", name = "Bitcoin", marketCapRank = 1, marketData = null),
            ),
        )

        state.market.isLoading shouldBe false
        state.marketChart.isLoading shouldBe false
        state.marketDetail.isLoading shouldBe false
    }

    "Given a chart, Then rangeChange is the percent change from its first to its last price" {
        fun stateWith(vararg prices: Double) = MarketDetailContract.State(
            marketChart = LoadableData.Loaded(MarketChart(persistentListOf(*prices.mapIndexed { i, p -> i.toLong() to p }.toTypedArray()))),
        )

        MarketDetailContract.State().chartRange shouldBe MarketDetailContract.ChartRange.Day
        MarketDetailContract.State().rangeChange shouldBe null
        stateWith(100.0).rangeChange shouldBe null
        stateWith(0.0, 5.0).rangeChange shouldBe null
        stateWith(200.0, 50.0, 150.0).rangeChange shouldBe -25.0
    }

    "Given each chart range, Then it asks for 1, 7, 30 and 365 days" {
        MarketDetailContract.ChartRange.entries.map { it.days } shouldBe listOf(1, 7, 30, 365)
    }
})
