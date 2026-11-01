@file:Suppress("ktlint")

package ir.composenews.marketdetail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ir.composenews.testing.dispatcher.TestDispatcherProvider
import ir.composenews.testing.repository.FakeMarketRepository
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.use_case.GetMarketByIdUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Resource
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class MarketDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createMockViewModel(): MarketDetailViewModel {
        val marketDetail = MarketDetail(id = "1", marketCapRank = 2, name = "name", marketData = null)
        val repository =
            FakeMarketRepository().apply {
                chart = { _, _ -> flowOf(Resource.Success(MarketChart(persistentListOf(0L to 50000.0)))) }
                detail = { flowOf(Resource.Success(marketDetail)) }
            }
        return MarketDetailViewModel(
            getMarketChartUseCase = GetMarketChartUseCase(repository),
            getMarketDetailUseCase = GetMarketDetailUseCase(repository),
            toggleFavoriteMarketListUseCase = ToggleFavoriteMarketListUseCase(repository),
            getMarketByIdUseCase = GetMarketByIdUseCase(repository),
            dispatcherProvider = TestDispatcherProvider(),
        )
    }

    @Test
    fun marketDetailScreen_DisplaysMarketDataCorrectly() {
        val mockViewModel = createMockViewModel()

        val marketModel =
            MarketModel(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 50000.0,
                priceChangePercentage24h = 5.0,
                imageUrl = "https://example.com/bitcoin.png",
                isFavorite = false,
            )
        composeTestRule.setContent {
            MarketDetailRoute(market = marketModel, viewModel = mockViewModel)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Bitcoin").assertExists()
        composeTestRule.onNodeWithText("50,000.00 $").assertExists()
        composeTestRule.onNodeWithText("Rank").assertExists()
    }

    @Test
    fun marketDetailScreen_FavoriteTogglesCorrectly() {
        val mockViewModel = createMockViewModel()
        val marketModel =
            MarketModel(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                isFavorite = false,
                currentPrice = 50000.0,
                priceChangePercentage24h = 5.0,
                imageUrl = "",
            )
        composeTestRule.setContent {
            MarketDetailRoute(market = marketModel, viewModel = mockViewModel)
        }
        composeTestRule.onNodeWithText("Favorite").performClick()
    }

//    @Test
//    fun marketDetailScreen_HandlesDataLoadingAndErrors() {
//        composeTestRule.setContent {
//            MarketDetailRoute(market = null)
//        }
//        composeTestRule.onNodeWithText("Loading...").assertExists()
//        composeTestRule.onNodeWithText("Failed to load data").assertExists()
//        composeTestRule.onNodeWithText("Retry").assertExists()
//    }

    @Test
    fun marketDetailScreen_UserInteractsWithChartData() {
        val mockViewModel = createMockViewModel()
        val marketModel =
            MarketModel(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 50000.0,
                priceChangePercentage24h = 5.0,
                imageUrl = "https://example.com/bitcoin.png",
                isFavorite = false,
            )
        composeTestRule.setContent {
            MarketDetailRoute(market = marketModel, viewModel = mockViewModel)
        }
    }

    @Test
    fun marketDetailScreen_NavigationPreservesState() {
        val mockViewModel = createMockViewModel()
        val marketModel =
            MarketModel(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 50000.0,
                priceChangePercentage24h = 5.0,
                imageUrl = "https://example.com/bitcoin.png",
                isFavorite = false,
            )
        composeTestRule.setContent {
            MarketDetailRoute(market = marketModel, viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Favorite").performClick()
        composeTestRule.onNodeWithText("Favorite").assertExists()
    }
}
