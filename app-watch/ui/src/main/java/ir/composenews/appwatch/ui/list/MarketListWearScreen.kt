@file:Suppress("ktlint")

package ir.composenews.appwatch.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import ir.composenews.base.use
import ir.composenews.designsystem.component.pull_refresh_indicator.pullRefresh
import ir.composenews.designsystem.component.pull_refresh_indicator.rememberPullRefreshState
import ir.composenews.designsystem.widget.ErrorView
import ir.composenews.extensions.roundToTwoDecimalPlaces
import ir.composenews.marketlist.MarketListContract
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.uimarket.model.MarketModel

@Composable
fun MarketListWearRoute(
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    val viewModel: MarketListViewModel = koinViewModel()
    val (state, event) = use(viewModel = viewModel)
    LaunchedEffect(key1 = Unit) {
        event.invoke(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = showFavoriteList))
        if (!showFavoriteList) {
            event.invoke(MarketListContract.Event.OnGetMarketList)
        }
    }
    val lazyPagingItems = viewModel.pagedMarketList.collectAsLazyPagingItems()
    MarketListWearScreen(
        lazyPagingItems = lazyPagingItems,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
    )
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
private fun MarketListWearScreen(
    lazyPagingItems: LazyPagingItems<MarketModel>,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { lazyPagingItems.refresh() },
    )
    val listState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Card,
            last = ScalingLazyColumnDefaults.ItemType.Card,
        ),
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .pullRefresh(refreshState),
    ) {
        when (val refreshLoadState = lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                ShimmerMarketListItem()
            }
            is LoadState.Error -> {
                ErrorView(errorMessage = refreshLoadState.error.message ?: "Unknown error")
            }
            else -> {
                ScalingLazyColumn(
                    columnState = listState,
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                    ) { index ->
                        val market = lazyPagingItems[index]
                        if (market != null) {
                            MarketItem(
                                modifier = modifier,
                                name = market.name,
                                symbol = market.symbol,
                                urlToImage = market.imageUrl,
                                price = market.currentPrice.toString(),
                                priceChangePercentage24h =
                                market.priceChangePercentage24h.roundToTwoDecimalPlaces(),
                                onItemClick = { onNavigateToDetailScreen(market) },
                            )
                        }
                    }
                }
            }
        }
    }
}
