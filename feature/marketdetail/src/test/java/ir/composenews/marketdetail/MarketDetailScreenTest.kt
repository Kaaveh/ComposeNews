@file:Suppress(
    "FunctionNaming",
    "LongParameterList",
    "ktlint:standard:function-naming",
    "ktlint:standard:multiline-expression-wrapping",
    "MaxLineLength",
)

package ir.composenews.marketdetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import ir.composenews.base.LoadableData
import ir.composenews.uimarket.model.MarketModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class MarketDetailScreenTest {
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
    fun givenLoadedMarketState_whenRendered_thenMarketNameIsVisible() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel()),
                ),
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
    }

    @Test
    fun givenLoadedMarketState_whenRendered_thenMarketPriceIsVisible() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel()),
                ),
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("50000.0 $").assertIsDisplayed()
    }

    @Test
    fun givenLoadedMarketStateWithFavorite_whenRendered_thenFavoritedMarketNameIsVisible() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel(isFavorite = true)),
                ),
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
    }

    @Test
    fun givenLoadedMarketStateWithFavorite_whenRendered_thenFavoriteIconIsRed() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel(isFavorite = true)),
                ),
                onFavoriteClick = {},
            )
        }
        composeTestRule
            .onNodeWithContentDescription("Favorited")
            .assertExists()
    }
}
