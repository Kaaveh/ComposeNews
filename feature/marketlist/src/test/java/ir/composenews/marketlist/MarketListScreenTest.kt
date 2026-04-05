@file:Suppress(
    "FunctionNaming",
    "LongParameterList",
    "ktlint:standard:function-naming",
    "ktlint:standard:multiline-expression-wrapping",
    "MaxLineLength",
)

package ir.composenews.marketlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ir.composenews.base.LoadableData
import ir.composenews.network.Errors
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class MarketListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun buildMarketModel(
        id: String = "btc",
        name: String = "Bitcoin",
        symbol: String = "BTC",
        currentPrice: Double = 50000.0,
        priceChangePercentage24h: Double = 2.5,
        imageUrl: String = "",
        isFavorite: Boolean = false,
    ) = MarketModel(
        id = id,
        name = name,
        symbol = symbol,
        currentPrice = currentPrice,
        priceChangePercentage24h = priceChangePercentage24h,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
    )

    @Test
    fun givenLoadedStateWithMarkets_whenRendered_thenMarketNameIsVisible() {
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = MarketListContract.State(
                    favoriteMarketList = LoadableData.Loaded(persistentListOf(buildMarketModel())),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
    }

    @Test
    fun givenEmptyFavoritesState_whenShowFavoriteListIsTrue_thenEmptyMessageIsVisible() {
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = MarketListContract.State(
                    favoriteMarketList = LoadableData.Loaded(persistentListOf()),
                    showFavoriteList = true,
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
        composeTestRule.onNodeWithText("Your favorite list is empty").assertIsDisplayed()
    }

    @Test
    fun givenErrorState_whenRendered_thenErrorMessageIsVisible() {
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = MarketListContract.State(
                    favoriteMarketList = LoadableData.Error(
                        Errors.ApiError(message = "Network Error", code = 500),
                    ),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
        composeTestRule.onNodeWithText("Network Error").assertIsDisplayed()
    }

    @Test
    fun givenLoadedState_whenItemIsClicked_thenNavigateCallbackIsInvoked() {
        val market = buildMarketModel()
        var navigatedMarket: MarketModel? = null
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = MarketListContract.State(
                    favoriteMarketList = LoadableData.Loaded(persistentListOf(market)),
                ),
                onNavigateToDetailScreen = { navigatedMarket = it },
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").performClick()
        assertEquals(market, navigatedMarket)
    }
}
