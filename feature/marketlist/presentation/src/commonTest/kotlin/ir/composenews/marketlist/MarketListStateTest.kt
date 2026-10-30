@file:Suppress("ktlint")

package ir.composenews.marketlist

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import ir.composenews.base.LoadableData
import ir.composenews.base.isLoading
import kotlinx.collections.immutable.persistentListOf

class MarketListStateTest : StringSpec({

    "Given default state, Then favoriteMarketList is Initial and showFavoriteList is false" {
        val state = MarketListContract.State()

        state.favoriteMarketList.shouldBeInstanceOf<LoadableData.Initial>()
        state.showFavoriteList shouldBe false
    }

    "Given state with showFavoriteList false, When copied with true, Then new state reflects the change" {
        val original = MarketListContract.State()

        val updated = original.copy(showFavoriteList = true)

        updated.showFavoriteList shouldBe true
        original.showFavoriteList shouldBe false
    }

    "Given a Loaded favoriteMarketList, When isLoading is checked, Then it returns false" {
        val state = MarketListContract.State(
            favoriteMarketList = LoadableData.Loaded(persistentListOf()),
        )

        state.favoriteMarketList.isLoading shouldBe false
    }

    "Given a Loading favoriteMarketList, When isLoading is checked, Then it returns true" {
        val state = MarketListContract.State(
            favoriteMarketList = LoadableData.Loading,
        )

        state.favoriteMarketList.isLoading shouldBe true
    }
})
