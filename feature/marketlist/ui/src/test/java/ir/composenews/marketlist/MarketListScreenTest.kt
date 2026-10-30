@file:Suppress(
    "FunctionNaming",
    "LongParameterList",
    "ktlint:standard:function-naming",
    "ktlint:standard:multiline-expression-wrapping",
    "MaxLineLength",
)

package ir.composenews.marketlist

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import ir.composenews.base.LoadableData
import ir.composenews.network.Errors
import ir.composenews.testing.fixture.buildMarketModel
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

    @Test
    fun givenLoadedStateWithMarkets_whenRendered_thenMarketNameIsVisible() {
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loaded(persistentListOf(buildMarketModel())),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
    }

    @Test
    fun givenEmptyFavoritesState_whenRendered_thenEmptyMessageIsVisible() {
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loaded(persistentListOf()),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("Your favorite list is empty").assertIsDisplayed()
    }

    @Test
    fun givenErrorState_whenRendered_thenErrorMessageIsVisible() {
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Error(
                        Errors.ApiError(message = "Network Error", code = 500),
                    ),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
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
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loaded(persistentListOf(market)),
                ),
                onNavigateToDetailScreen = { navigatedMarket = it },
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").performClick()
        assertEquals(market, navigatedMarket)
    }

    @Test
    fun givenLoadedState_whenFavoriteIconClicked_thenFavoriteCallbackInvoked() {
        val market = buildMarketModel()
        var clickedMarket: MarketModel? = null
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loaded(persistentListOf(market)),
                ),
                onNavigateToDetailScreen = { },
                onFavoriteClick = { clickedMarket = it },
            )
        }
        composeTestRule.onNodeWithContentDescription("Not favorited").performClick()
        assertEquals(market, clickedMarket)
    }

    @Test
    fun givenLoadingState_whenFavoritesAreLoading_NoItemInScreen() {
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loading,
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").assertDoesNotExist()
    }

    @Test
    fun givenLoadedMarkets_whenSwipeRefreshTriggered_thenOnRefreshCallbackInvoked() {
        val market = buildMarketModel()
        var refreshCount = 0
        composeTestRule.setContent {
            MarketListScreen(
                state = MarketListContract.State(
                    marketList = LoadableData.Loaded(persistentListOf(market)),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {
                    refreshCount++
                },
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").performTouchInput {
            down(center)
            moveBy(Offset(0f, 400f))
            up()
        }
        composeTestRule.waitForIdle()
        assertEquals(1, refreshCount)
    }

    @Test
    fun givenRateLimitedMarkets_whenRendered_thenTooManyRequestsIsVisible() {
        composeTestRule.setContent {
            MarketListScreen(
                state = MarketListContract.State(
                    marketList = LoadableData.Error(Errors.RateLimited(retryAfterSeconds = 42)),
                    retryInSeconds = 42,
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
        composeTestRule.onNodeWithText("Too many requests.").assertIsDisplayed()
    }

    @Test
    fun givenLoadedMultipleItems_whenRendered_thenItemsAreDisplayed() {
        val bitcoin = buildMarketModel()
        val ethereum = buildMarketModel("eth", "Ethereum", "ETH")
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loaded(persistentListOf(bitcoin, ethereum)),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ethereum").assertIsDisplayed()
    }

    @Test
    fun givenFavoriteLoadedState_whenUserSwipedItem_thenOnFavoriteCallbackInvoked() {
        val bitcoin = buildMarketModel()
        var removedMarket: MarketModel? = null
        composeTestRule.setContent {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loaded(persistentListOf(bitcoin)),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {
                    removedMarket = it
                },
            )
        }

        composeTestRule.onNodeWithText("Bitcoin").performTouchInput {
            down(center)
            moveBy(Offset(-300f, 0f))
            up()
        }

        composeTestRule.waitUntil(1000) {
            removedMarket != null
        }

        assertEquals(bitcoin, removedMarket)
    }
}
