@file:Suppress("ktlint")

package ir.composenews.marketlist

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ir.composenews.base.LoadableData
import ir.composenews.marketlist.MarketListContract.Filter
import ir.composenews.marketlist.MarketListContract.Header
import ir.composenews.marketlist.MarketListContract.Sort
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf

class MarketListStateTest : StringSpec({

    "Given default state, Then the list is Initial, unfiltered and sorted by market cap" {
        val state = MarketListContract.State()

        state.marketList.shouldBeInstanceOf<LoadableData.Initial>()
        state.filter shouldBe Filter.All
        state.sort shouldBe Sort.MarketCap
        state.topMover shouldBe null
    }

    "Given each filter and sort, Then the header names the list" {
        MarketListContract.State().header shouldBe Header.ByMarketCap
        MarketListContract.State(sort = Sort.Volume).header shouldBe Header.ByVolume
        MarketListContract.State(filter = Filter.Gainers, sort = Sort.Volume).header shouldBe Header.Gainers
        MarketListContract.State(filter = Filter.Losers).header shouldBe Header.Losers
    }

    "Given favorites, Then count is their number" {
        val favorite = MarketModel("btc", "Bitcoin", "BTC", 1.0, 1.0, "", isFavorite = true)

        FavoriteListContract.State().count shouldBe 0
        FavoriteListContract.State(favoriteList = LoadableData.Loaded(persistentListOf(favorite))).count shouldBe 1
    }

    "Given markets without rank or volume, When sorting, Then they go last" {
        val ranked = MarketModel("btc", "Bitcoin", "BTC", 1.0, 1.0, "", marketCapRank = 1, totalVolume = 5.0)
        val unranked = MarketModel("new", "New", "NEW", 1.0, 1.0, "")

        listOf(unranked, ranked).filteredAndSorted(Filter.All, Sort.MarketCap) shouldBe listOf(ranked, unranked)
        listOf(unranked, ranked).filteredAndSorted(Filter.All, Sort.Volume) shouldBe listOf(ranked, unranked)
    }

    "Given a market with no 24h change, When filtering, Then it is neither a gainer nor a loser" {
        val flat = MarketModel("usdt", "Tether", "USDT", 1.0, 0.0, "")

        listOf(flat).filteredAndSorted(Filter.Gainers, Sort.MarketCap) shouldBe emptyList()
        listOf(flat).filteredAndSorted(Filter.Losers, Sort.MarketCap) shouldBe emptyList()
        listOf(flat).topMover() shouldBe null
    }
})
