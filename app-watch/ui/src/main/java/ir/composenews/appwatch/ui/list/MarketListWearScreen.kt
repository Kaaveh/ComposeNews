@file:Suppress("ktlint")

package ir.composenews.appwatch.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import androidx.wear.compose.foundation.lazy.items
import ir.composenews.base.LoadableData
import ir.composenews.base.errorViewMapper
import ir.composenews.core.ui.LoadableComponent
import ir.composenews.core.ui.use
import ir.composenews.designsystem.component.pull_refresh_indicator.pullRefresh
import ir.composenews.designsystem.component.pull_refresh_indicator.rememberPullRefreshState
import ir.composenews.designsystem.widget.ErrorView
import ir.composenews.extensions.roundToTwoDecimalPlaces
import ir.composenews.marketlist.FavoriteListContract
import ir.composenews.marketlist.FavoriteListViewModel
import ir.composenews.marketlist.MarketListContract
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList

@Composable
fun MarketListWearRoute(
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    if (showFavoriteList) {
        val (state, event) = use(viewModel = koinViewModel<FavoriteListViewModel>())
        LaunchedEffect(key1 = Unit) {
            event.invoke(FavoriteListContract.Event.OnGetFavoriteList)
        }
        MarketListWearScreen(
            marketList = state.favoriteList,
            isRefreshing = false,
            onRefresh = {},
            onNavigateToDetailScreen = onNavigateToDetailScreen,
        )
    } else {
        val (state, event) = use(viewModel = koinViewModel<MarketListViewModel>())
        LaunchedEffect(key1 = Unit) {
            event.invoke(MarketListContract.Event.OnGetMarketList)
        }
        MarketListWearScreen(
            marketList = state.marketList,
            isRefreshing = state.isRefreshing,
            onRefresh = { event.invoke(MarketListContract.Event.OnGetMarketList) },
            onNavigateToDetailScreen = onNavigateToDetailScreen,
        )
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
private fun MarketListWearScreen(
    marketList: LoadableData<PersistentList<MarketModel>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
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
        LoadableComponent(
            loadableData = marketList,
            loading = { ShimmerMarketListItem() },
            error = { error -> ErrorView(errorMessage = errorViewMapper(error)) },
            loaded = { markets ->
                ScalingLazyColumn(
                    columnState = listState,
                ) {
                    items(
                        items = markets,
                        key = { it.id },
                    ) { market ->
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
            },
        )
    }
}
