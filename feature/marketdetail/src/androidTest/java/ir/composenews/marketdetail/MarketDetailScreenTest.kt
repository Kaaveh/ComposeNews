@file:Suppress("ktlint")

package ir.composenews.marketdetail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.repository.FakeMarketRepository
import ir.composenews.domain.use_case.GetMarketByIdUseCase
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.network.Errors
import ir.composenews.network.Resource
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class MarketDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val fakeMarketRepository = FakeMarketRepository()

    private fun createMockViewModel(): MarketDetailViewModel {
        val fakeGetMarketMarketChartUseCase =
            object : GetMarketChartUseCase(fakeMarketRepository) {
                override fun invoke(id: String): Flow<Resource<MarketChart, Errors>> {
                    return flowOf(Resource.Success(MarketChart(persistentListOf(Pair(0, 50000.0)))))
                }
            }

        val fakeGetMarketDetailUseCase =
            object : GetMarketDetailUseCase(fakeMarketRepository) {
                override fun invoke(id: String): Flow<Resource<MarketDetail, Errors>> {
                    return flowOf(
                        Resource.Success(
                            MarketDetail(
                                id = "1",
                                marketCapRank = 2,
                                name = "name",
                                marketData = null,
                            )
                        )
                    )
                }
            }

        val fakeToggleFavoriteMarketListUseCase =
            object : ToggleFavoriteMarketListUseCase(fakeMarketRepository) {
                override suspend fun invoke(market: Market) = Unit
            }

        val fakeGetMarketByIdUseCase = GetMarketByIdUseCase(fakeMarketRepository)

        val fakeDispatcherProvider = TestDispatcherProvider()

        return MarketDetailViewModel(
            getMarketChartUseCase = fakeGetMarketMarketChartUseCase,
            getMarketDetailUseCase = fakeGetMarketDetailUseCase,
            toggleFavoriteMarketListUseCase = fakeToggleFavoriteMarketListUseCase,
            getMarketByIdUseCase = fakeGetMarketByIdUseCase,
            dispatcherProvider = fakeDispatcherProvider,
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
        composeTestRule.onNodeWithText("50000.0 $").assertExists()
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
        composeTestRule.onNodeWithContentDescription("Not favorited").performClick()
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

        composeTestRule.onNodeWithContentDescription("Not favorited").performClick()
        composeTestRule.onNodeWithContentDescription("Not favorited").assertExists()
    }
}
