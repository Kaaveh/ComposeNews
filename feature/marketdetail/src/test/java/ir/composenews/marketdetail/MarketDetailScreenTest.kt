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
import androidx.compose.ui.test.performClick
import ir.composenews.base.LoadableData
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

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

    @Test
    fun givenLoadedMarketState_whenFavoriteClicked_thenFavoriteCallbackIsInvoked() {
        val bitcoin = buildMarketModel(isFavorite = false)
        var clickedMarket: MarketModel? = null
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(bitcoin),
                ),
                onFavoriteClick = { clickedMarket = it },
            )
        }

        composeTestRule.onNodeWithContentDescription("Not favorited").performClick()
        assertEquals(bitcoin, clickedMarket)
    }

    @Test
    fun givenLoadedMarketState_whenRendered_thenStatRowsAreVisible() {
        val bitcoin = buildMarketModel()
        val bitcoinDetail = MarketDetail(
            id = bitcoin.id,
            marketCapRank = 1,
            marketData = MarketDetail.MarketData(
                high24hUSD = 100_000.0,
                low24hUSD = 50_000.0,
                marketCapUSD = 255_000_000_000,
                marketCapRank = 1,
            ),
            name = bitcoin.name,
        )
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(bitcoin),
                    marketChart = LoadableData.Loaded(
                        MarketChart(
                            prices = persistentListOf(1L to 100.0, 2L to 200.0),
                        ),
                    ),
                    marketDetail = LoadableData.Loaded(bitcoinDetail),
                ),
                onFavoriteClick = {},
            )
        }

        composeTestRule.onNodeWithText("Market Cap").assertExists()
        composeTestRule.onNodeWithText("$255B").assertExists()
        composeTestRule.onNodeWithText("High 24h").assertExists()
        composeTestRule.onNodeWithText("100000.0").assertExists()
        composeTestRule.onNodeWithText("Low 24h").assertExists()
        composeTestRule.onNodeWithText("50000.0").assertExists()
        composeTestRule.onNodeWithText("Rank").assertExists()
        composeTestRule.onNodeWithText("#1").assertExists()
    }

    @Test
    fun givenLoadingState_whenNotLoadedYet_thenNoNameDisplayed() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loading,
                ),
                onFavoriteClick = {},
            )
        }

        composeTestRule.onNodeWithText("Bitcoin").assertDoesNotExist()
    }

    @Test
    fun givenLoadedMarketState_whenRendered_thenChartIsDisplayed() {
        val bitcoin = buildMarketModel()
        val bitcoinDetail = MarketDetail(
            id = bitcoin.id,
            marketCapRank = 1,
            marketData = MarketDetail.MarketData(
                high24hUSD = 100_000.0,
                low24hUSD = 50_000.0,
                marketCapUSD = 255_000_000_000,
                marketCapRank = 1,
            ),
            name = bitcoin.name,
        )

        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(bitcoin),
                    marketChart = LoadableData.Loaded(
                        MarketChart(
                            prices = persistentListOf(1L to 100.0, 2L to 200.0),
                        ),
                    ),
                    marketDetail = LoadableData.Loaded(bitcoinDetail),
                ),
                onFavoriteClick = {},
            )
        }
    }

    @Test
    fun givenEmptyChartData_whenRendered_thenEmptyTextIsVisible() {
        val bitcoin = buildMarketModel()
        val bitcoinDetail = MarketDetail(
            id = bitcoin.id,
            marketCapRank = 1,
            marketData = MarketDetail.MarketData(
                high24hUSD = 100_000.0,
                low24hUSD = 50_000.0,
                marketCapUSD = 255_000_000_000,
                marketCapRank = 1,
            ),
            name = bitcoin.name,
        )
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(bitcoin),
                    marketChart = LoadableData.Loaded(MarketChart(persistentListOf())),
                    marketDetail = LoadableData.Loaded(bitcoinDetail),
                ),
                onFavoriteClick = {},
            )
        }

        composeTestRule.onNodeWithText("No chart data").assertExists()
        composeTestRule.onNodeWithText("No chart data").assertIsDisplayed()
    }
}
