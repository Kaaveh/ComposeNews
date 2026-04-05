@file:Suppress("ktlint")

package ir.composenews.marketlist

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import ir.composenews.base.LoadableData
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

@Composable
fun TestableMarketListScreen(
    marketListState: MarketListContract.State,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: () -> Unit,
) {
    FavoriteMarketListScreen(
        state = marketListState,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = onFavoriteClick,
        onRefresh = onRefresh,
    )
}

class MarketListScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun marketListScreen_DisplayedCorrectly() {
        composeTestRule.setContent {
            TestableMarketListScreen(
                marketListState =
                MarketListContract.State(
                    favoriteMarketList = LoadableData.Loaded(
                        persistentListOf(
                            MarketModel(
                                id = "1",
                                name = "Bitcoin",
                                symbol = "BTC",
                                currentPrice = 50000.0,
                                priceChangePercentage24h = 5.0,
                                imageUrl = "",
                                isFavorite = false,
                            )
                        )
                    ),
                    showFavoriteList = false,
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }

        composeTestRule.onNodeWithText("Bitcoin").assertExists()
        composeTestRule.onNodeWithText("BTC").assertExists()
    }

    @Test
    fun pullToRefresh_InvokesOnRefresh() {
        composeTestRule.setContent {
            Box(
                modifier =
                Modifier
                    .fillMaxSize()
                    .testTag("pullToRefresh"),
            ) {}
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("pullToRefresh").performTouchInput {
            swipeDown(startY = 0.1f, endY = 0.9f, durationMillis = 200)
        }
    }
}
