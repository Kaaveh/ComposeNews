@file:Suppress(
    "FunctionNaming",
    "LongParameterList",
    "ktlint:standard:function-naming",
    "ktlint:standard:multiline-expression-wrapping",
    "MaxLineLength",
)

package ir.composenews.marketlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import ir.composenews.uimarket.model.MarketModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Composable
fun TestablePagedMarketListScreen(
    pagedData: PagingData<MarketModel>,
    onNavigateToDetailScreen: (MarketModel) -> Unit,
    onFavoriteClick: (MarketModel) -> Unit
) {
    val lazyPagingItems = flowOf(pagedData).collectAsLazyPagingItems()
    PagedMarketListScreen(
        lazyPagingItems = lazyPagingItems,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = onFavoriteClick
    )
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class PagedMarketListScreenTest {

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
    fun givenRefreshError_whenRendered_thenErrorMessageIsVisible() {
        val pagingData = PagingData.from<MarketModel>(
            data = emptyList(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.Error(Exception("Network failed")),
                prepend = LoadState.NotLoading(endOfPaginationReached = false),
                append = LoadState.NotLoading(endOfPaginationReached = false)
            )
        )

        composeTestRule.setContent {
            TestablePagedMarketListScreen(
                pagedData = pagingData,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {}
            )
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Network failed").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bitcoin").assertDoesNotExist()
    }

}