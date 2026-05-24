@file:Suppress("ktlint")

package ir.composenews.apptv.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import ir.composenews.base.LoadableComponent
import ir.composenews.base.errorViewMapper
import ir.composenews.base.use
import ir.composenews.designsystem.component.EmptyStateAnimation
import ir.composenews.designsystem.widget.ErrorView
import ir.composenews.marketlist.MarketListContract
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.uimarket.model.MarketModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketListTvRoute(
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    val viewModel: MarketListViewModel = koinViewModel(
        key = if (showFavoriteList) "favorites" else "markets",
    )
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(Unit) {
        event(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = showFavoriteList))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 24.dp),
    ) {
        if (showFavoriteList) {
            FavoriteMarketListTvScreen(
                state = state,
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                onFavoriteClick = { market ->
                    event(MarketListContract.Event.OnFavoriteClick(market = market))
                },
            )
        } else {
            val lazyPagingItems = viewModel.pagedMarketList.collectAsLazyPagingItems()
            PagedMarketListTvScreen(
                lazyPagingItems = lazyPagingItems,
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                onFavoriteClick = { market ->
                    event(MarketListContract.Event.OnFavoriteClick(market = market))
                },
            )
        }
    }
}

@Composable
private fun PagedMarketListTvScreen(
    lazyPagingItems: LazyPagingItems<MarketModel>,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
) {
    when (val refreshState = lazyPagingItems.loadState.refresh) {
        is LoadState.Error -> {
            ErrorView(errorMessage = refreshState.error.message ?: "Unknown error")
        }
        else -> {
            val itemCount = lazyPagingItems.itemCount
            val hasAppendIndicator = lazyPagingItems.loadState.append is LoadState.Loading ||
                lazyPagingItems.loadState.append is LoadState.Error
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    count = itemCount,
                    key = lazyPagingItems.itemKey { it.name },
                ) { index ->
                    val market = lazyPagingItems[index]
                    if (market != null) {
                        val isFirst = index == 0
                        val isLast = index == itemCount - 1 && !hasAppendIndicator
                        MarketListItem(
                            modifier = focusEdgeModifier(isFirst = isFirst, isLast = isLast),
                            market = market,
                            showFavoriteList = false,
                            onItemClick = { onNavigateToDetailScreen(market) },
                            onFavoriteClick = { onFavoriteClick(market) },
                        )
                    }
                }
                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                if (lazyPagingItems.loadState.append is LoadState.Error) {
                    item {
                        ErrorView(
                            errorMessage = (lazyPagingItems.loadState.append as LoadState.Error)
                                .error.message ?: "Unknown error",
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteMarketListTvScreen(
    state: MarketListContract.State,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
) {
    LoadableComponent(
        loadableData = state.favoriteMarketList,
        loaded = { data ->
            if (data.isEmpty() && state.showFavoriteList) {
                EmptyStateAnimation()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(items = data, key = { _, m -> m.name }) { index, market ->
                        val isFirst = index == 0
                        val isLast = index == data.lastIndex
                        MarketListItem(
                            modifier = focusEdgeModifier(isFirst = isFirst, isLast = isLast),
                            market = market,
                            showFavoriteList = true,
                            onItemClick = { onNavigateToDetailScreen(market) },
                            onFavoriteClick = { onFavoriteClick(market) },
                        )
                    }
                }
            }
        },
        error = { error ->
            ErrorView(errorMessage = errorViewMapper(error))
        },
    )
}

private fun focusEdgeModifier(isFirst: Boolean, isLast: Boolean): Modifier =
    Modifier.focusProperties {
        if (isFirst) up = FocusRequester.Cancel
        if (isLast) down = FocusRequester.Cancel
    }
