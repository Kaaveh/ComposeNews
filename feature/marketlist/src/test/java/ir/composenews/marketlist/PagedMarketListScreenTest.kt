@file:Suppress(
    "FunctionNaming",
    "LongParameterList",
    "ktlint:standard:function-naming",
    "ktlint:standard:multiline-expression-wrapping",
    "MaxLineLength",
)

package ir.composenews.marketlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import ir.composenews.core_test.fixture.MarketModelFixtures.buildMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Composable
internal fun TestablePagedMarketListScreen(
    pagedData: PagingData<MarketModel>,
    onNavigateToDetailScreen: (MarketModel) -> Unit,
    onFavoriteClick: (MarketModel) -> Unit,
) {
    val lazyPagingItems = flowOf(pagedData).collectAsLazyPagingItems()
    PagedMarketListScreen(
        lazyPagingItems = lazyPagingItems,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = onFavoriteClick,
    )
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class PagedMarketListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenRefreshError_whenRendered_thenErrorMessageIsVisible() {
        val pagingData = PagingData.from<MarketModel>(
            data = emptyList(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.Error(Exception("Network failed")),
                prepend = LoadState.NotLoading(endOfPaginationReached = false),
                append = LoadState.NotLoading(endOfPaginationReached = false),
            ),
        )

        composeTestRule.setContent {
            TestablePagedMarketListScreen(
                pagedData = pagingData,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }

        composeTestRule.waitUntilExists(hasText("Network failed"))
        composeTestRule.onNodeWithText("Network failed").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bitcoin").assertDoesNotExist()
    }

    @Test
    fun givenLoadedItems_whenRendered_thenMarketNamesAreVisible() {
        val bitcoin = buildMarketModel()
        val ethereum = buildMarketModel("eth", "Ethereum", "ETH")
        val pagingData = PagingData.from(listOf(bitcoin, ethereum))
        composeTestRule.setContent {
            TestablePagedMarketListScreen(
                pagedData = pagingData,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }
        composeTestRule.waitUntilExists(hasText("Bitcoin"))
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
        composeTestRule.onNodeWithText("BTC").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ethereum").assertIsDisplayed()
    }

    @Test
    fun givenLoadedItems_whenItemClicked_thenNavigateCallbackIsInvoked() {
        val bitcoin = buildMarketModel()
        val pagingData = PagingData.from(listOf(bitcoin))
        var navigatedMarket: MarketModel? = null
        composeTestRule.setContent {
            TestablePagedMarketListScreen(
                pagedData = pagingData,
                onNavigateToDetailScreen = { navigatedMarket = it },
                onFavoriteClick = {},
            )
        }
        composeTestRule.waitUntilExists(hasText("Bitcoin"))
        composeTestRule.onNodeWithText("Bitcoin").performClick()
        assertEquals(bitcoin, navigatedMarket)
    }

    @Test
    fun givenLoadedItems_whenFavoriteClicked_thenFavoriteCallbackIsInvoked() {
        val bitcoin = buildMarketModel()
        val pagingData = PagingData.from(listOf(bitcoin))
        var clickedMarket: MarketModel? = null
        composeTestRule.setContent {
            TestablePagedMarketListScreen(
                pagedData = pagingData,
                onNavigateToDetailScreen = {},
                onFavoriteClick = { clickedMarket = it },
            )
        }
        composeTestRule.waitUntilExists(hasContentDescription("Not favorited"))
        composeTestRule.onNodeWithContentDescription("Not favorited").performClick()
        composeTestRule.waitForIdle()
        assertEquals(bitcoin, clickedMarket)
    }

    @Test
    fun givenAppendLoading_whenRendered_thenLoadingIndicatorIsShown() {
        val bitcoin = buildMarketModel()
        val pagingData = PagingData.from(
            data = listOf(bitcoin),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = true),
                prepend = LoadState.NotLoading(endOfPaginationReached = false),
                append = LoadState.Loading,
            ),
        )
        composeTestRule.setContent {
            TestablePagedMarketListScreen(
                pagedData = pagingData,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }

        composeTestRule.waitUntilExists(hasTestTag("Paging Progress Bar"))
        composeTestRule.onNodeWithTag("Paging Progress Bar").assertExists()
    }

    @Test
    fun givenAppendError_whenRendered_thenErrorIsVisible() {
        val bitcoin = buildMarketModel()
        val pagingData = PagingData.from(
            data = listOf(bitcoin),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = false),
                append = LoadState.Error(Exception("Could not load more")),
            ),
        )
        composeTestRule.setContent {
            TestablePagedMarketListScreen(
                pagedData = pagingData,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }
        composeTestRule.waitUntilExists(hasText("Could not load more"))
        composeTestRule.onNodeWithText("Could not load more").assertIsDisplayed()
    }
}

private fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 5_000,
) {
    waitUntil(timeoutMillis = timeoutMillis) {
        onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty()
    }
}
