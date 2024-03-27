package ir.composenews.marketdetail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.domain.model.Chart
import ir.composenews.domain.model.Market
import ir.composenews.domain.model.MarketDetail
import ir.composenews.domain.model.Resource
import ir.composenews.domain.repository.MarketRepository
import ir.composenews.domain.use_case.GetMarketChartUseCase
import ir.composenews.domain.use_case.GetMarketDetailUseCase
import ir.composenews.domain.use_case.ToggleFavoriteMarketListUseCase
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class MarketDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val fakeMarketRepository =
        object : MarketRepository {
            override fun getMarketList(): Flow<List<Market>> {
                TODO("Not yet implemented")
            }

            override fun getFavoriteMarketList(): Flow<List<Market>> {
                TODO("Not yet implemented")
            }

            override suspend fun syncMarketList() {
                TODO("Not yet implemented")
            }

            override suspend fun toggleFavoriteMarket(oldMarket: Market) {
                TODO("Not yet implemented")
            }

            override fun fetchChart(id: String): Flow<Resource<Chart>> {
                TODO("Not yet implemented")
            }

            override fun fetchDetail(id: String): Flow<Resource<MarketDetail>> {
                TODO("Not yet implemented")
            }
        }

    private fun createMockViewModel(): MarketDetailViewModel {
        val fakeGetMarketChartUseCase =
            object : GetMarketChartUseCase(fakeMarketRepository) {
                override fun invoke(id: String): Flow<Resource<Chart>> {
                    return flowOf(Resource.Success(Chart(persistentListOf(Pair(0, 50000.0)))))
                }
            }

        val fakeGetMarketDetailUseCase =
            object : GetMarketDetailUseCase(fakeMarketRepository) {
                override fun invoke(id: String): Flow<Resource<MarketDetail>> {
                    return flowOf(Resource.Success(MarketDetail(marketCapRank = 1)))
                }
            }

        val fakeToggleFavoriteMarketListUseCase =
            object : ToggleFavoriteMarketListUseCase(fakeMarketRepository) {
                override suspend fun invoke(market: Market) {
                    TODO("Not yet implemented")
                }
            }

        val fakeDispatcherProvider = FakeDispatcherProvider()

        return MarketDetailViewModel(
            getMarketChartUseCase = fakeGetMarketChartUseCase,
            getMarketDetailUseCase = fakeGetMarketDetailUseCase,
            toggleFavoriteMarketListUseCase = fakeToggleFavoriteMarketListUseCase,
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
        composeTestRule.onNodeWithText("$50,000.0").assertExists()
        composeTestRule.onNodeWithText("Rank").assertExists()
    }

    @Test
    fun marketDetailScreen_FavoriteTogglesCorrectly() {
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
            MarketDetailRoute(market = marketModel)
        }
        composeTestRule.onNodeWithText("Favorite").performClick()
    }

    @Test
    fun marketDetailScreen_HandlesDataLoadingAndErrors() {
        composeTestRule.setContent {
            MarketDetailRoute(market = null)
        }
        composeTestRule.onNodeWithText("Loading...").assertExists()
        composeTestRule.onNodeWithText("Failed to load data").assertExists()
        composeTestRule.onNodeWithText("Retry").assertExists()
    }

    @Test
    fun marketDetailScreen_UserInteractsWithChartData() {
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
            MarketDetailRoute(market = marketModel)
        }
    }

    @Test
    fun marketDetailScreen_NavigationPreservesState() {
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
            MarketDetailRoute(market = marketModel)
        }

        composeTestRule.onNodeWithText("Favorite").performClick()
        composeTestRule.onNodeWithText("Favorite").assertExists()
        composeTestRule.onNodeWithContentDescription("Favorite Icon").assertExists()
    }
}

class FakeDispatcherProvider : DispatcherProvider {
    override val ui: CoroutineDispatcher = Dispatchers.Unconfined
    override val io: CoroutineDispatcher = Dispatchers.Unconfined
    override val bg: CoroutineDispatcher = Dispatchers.Unconfined
}
