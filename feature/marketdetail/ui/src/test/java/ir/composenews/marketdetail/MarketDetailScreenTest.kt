@file:Suppress(
    "FunctionNaming",
    "LongParameterList",
    "ktlint:standard:function-naming",
    "ktlint:standard:multiline-expression-wrapping",
    "MaxLineLength",
)

package ir.composenews.marketdetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import ir.composenews.base.LoadableData
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.marketdetail.components.MARKET_DETAIL_CHART_TEST_TAG
import ir.composenews.testing.fixture.buildMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class MarketDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenLoadedMarketState_whenRendered_thenMarketNameIsVisible() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel()),
                ),
                onFavoriteClick = {},
                onBack = {},
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
                onBack = {},
            )
        }
        composeTestRule.onNodeWithText("50,000.00 $").assertIsDisplayed()
    }

    @Test
    fun givenLoadedMarketStateWithFavorite_whenRendered_thenFabSaysSaved() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel(isFavorite = true)),
                ),
                onFavoriteClick = {},
                onBack = {},
            )
        }
        composeTestRule.onNodeWithText("Saved").assertExists()
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
                onBack = {},
            )
        }

        composeTestRule.onNodeWithText("Favorite").performClick()
        assertEquals(bitcoin, clickedMarket)
    }

    @Test
    fun givenLoadedMarket_whenRangeAndBackClicked_thenCallbacksAreInvoked() {
        var range: MarketDetailContract.ChartRange? = null
        var wentBack = false
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(market = LoadableData.Loaded(buildMarketModel())),
                onFavoriteClick = {},
                onBack = { wentBack = true },
                onChartRangeSelect = { range = it },
            )
        }
        composeTestRule.onNodeWithText("Today").assertIsDisplayed()
        // Back first: scrolling to the range group moves the top bar off screen.
        composeTestRule.onNodeWithContentDescription("Back to markets").performClick()
        composeTestRule.onNodeWithText("1W").performScrollTo().performClick()
        assertEquals(MarketDetailContract.ChartRange.Week, range)
        assertTrue(wentBack)
    }

    @Test
    fun givenNoBackOutsideAPane_whenRendered_thenItIsFullScreenWithTheFab() {
        // The TV detail: full-screen without a back button.
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(market = LoadableData.Loaded(buildMarketModel())),
                onFavoriteClick = {},
            )
        }
        composeTestRule.onNodeWithContentDescription("Back to markets").assertDoesNotExist()
        composeTestRule.onNodeWithText("Favorite").assertIsDisplayed()
    }

    @Test
    fun givenInPane_whenRendered_thenItIsACardWithTheToggleInTheHeader() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(market = LoadableData.Loaded(buildMarketModel())),
                onFavoriteClick = {},
                inPane = true,
            )
        }
        composeTestRule.onNodeWithContentDescription("Back to markets").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Share").assertIsDisplayed()
        // A narrow pane: the heart alone, no FAB label.
        composeTestRule.onNodeWithContentDescription("Add Bitcoin to favorites").assertIsDisplayed()
        composeTestRule.onNodeWithText("Favorite").assertDoesNotExist()
    }

    @Test
    @Config(qualifiers = "w1280dp-h800dp")
    fun givenWidePane_whenRendered_thenToggleIsLabeledAndTilesShowInOneRow() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel(isFavorite = true)),
                    marketDetail = LoadableData.Loaded(bitcoinDetail()),
                ),
                onFavoriteClick = {},
                inPane = true,
            )
        }
        composeTestRule.onNodeWithText("Saved").assertIsDisplayed()
        composeTestRule.onNode(hasStateDescription("In favorites")).assertIsDisplayed()
        val cap = composeTestRule
            .onNodeWithText("$255B")
            .performScrollTo()
            .fetchSemanticsNode()
            .boundsInRoot
        val low = composeTestRule
            .onNodeWithText("50,000.00")
            .performScrollTo()
            .fetchSemanticsNode()
            .boundsInRoot
        assertEquals(cap.top, low.top)
    }

    @Test
    fun givenExtraPaneShowsMarketData_whenRendered_thenDetailLeavesItOut() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel()),
                    marketDetail = LoadableData.Loaded(bitcoinDetail()),
                ),
                onFavoriteClick = {},
                inPane = true,
                showMarketData = false,
            )
        }
        composeTestRule.onNodeWithText("Market Data").assertDoesNotExist()
    }

    @Test
    fun givenMarketDetail_whenMarketDataCardRendered_thenItListsTheValues() {
        composeTestRule.setContent {
            MarketDataCard(state = MarketDetailContract.State(marketDetail = LoadableData.Loaded(bitcoinDetail())))
        }
        listOf("Market Data", "Market Cap", "$255B", "Rank", "#1", "High 24h", "100,000.00", "Low 24h", "50,000.00").forEach {
            composeTestRule.onNodeWithText(it).assertIsDisplayed()
        }
    }

    @Test
    fun givenFallingChart_whenRendered_thenChipShowsTheDrop() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loaded(buildMarketModel()),
                    marketChart = LoadableData.Loaded(MarketChart(persistentListOf(1L to 200.0, 2L to 150.0))),
                ),
                onFavoriteClick = {},
                onBack = {},
            )
        }
        composeTestRule.onNodeWithContentDescription("Down 25.00 %").assertIsDisplayed()
    }

    @Test
    fun givenMarket_whenBuildingShareText_thenItHasPriceChangeAndLink() {
        assertEquals(
            "Bitcoin (BTC): 50,000.00 $, ▲ 2.50 % (24h)\nhttps://www.coingecko.com/en/coins/btc",
            shareText(buildMarketModel()),
        )
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
                onBack = {},
            )
        }

        assertStatRowsAreVisible("Market Cap", "$255B", "High 24h", "100,000.00", "Low 24h", "50,000.00", "Rank", "#1")
    }

    @Test
    fun givenLoadingState_whenNotLoadedYet_thenNoNameDisplayed() {
        composeTestRule.setContent {
            MarketDetailScreen(
                marketDetailState = MarketDetailContract.State(
                    market = LoadableData.Loading,
                ),
                onFavoriteClick = {},
                onBack = {},
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
                onBack = {},
            )
        }

        composeTestRule.onNodeWithTag(MARKET_DETAIL_CHART_TEST_TAG).assertIsDisplayed()
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
                onBack = {},
            )
        }

        composeTestRule.onNodeWithText("No chart data").assertExists()
        composeTestRule.onNodeWithText("No chart data").assertIsDisplayed()
    }

    private fun bitcoinDetail() =
        MarketDetail(
            id = "btc",
            marketCapRank = 1,
            marketData = MarketDetail.MarketData(
                high24hUSD = 100_000.0,
                low24hUSD = 50_000.0,
                marketCapUSD = 255_000_000_000,
                marketCapRank = 1,
            ),
            name = "Bitcoin",
        )

    private fun assertStatRowsAreVisible(vararg texts: String) {
        texts.forEach { text ->
            composeTestRule
                .onNodeWithText(text)
                .performScrollTo()
                .assertIsDisplayed()
        }
    }
}
