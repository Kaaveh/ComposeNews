@file:Suppress(
    "MaxLineLength",
    "ComplexCondition",
    "ktlint",
    "UnusedPrivateMember",
    "LongMethod",
)

package ir.composenews.marketlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.paging.compose.LazyPagingItems
import com.airbnb.lottie.compose.LottieCompositionSpec
import ir.composenews.base.LoadableComponent
import ir.composenews.base.errorViewMapper
import ir.composenews.base.isLoading
import ir.composenews.base.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.EmptyStateAnimation
import ir.composenews.designsystem.component.pull_refresh_indicator.PullRefreshIndicator
import ir.composenews.designsystem.component.pull_refresh_indicator.pullRefresh
import ir.composenews.designsystem.component.pull_refresh_indicator.rememberPullRefreshState
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.widget.ErrorView
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.marketlist.preview_provider.MarketListStateProvider
import ir.composenews.uimarket.model.MarketModel

@Composable
fun MarketListRoute(
    showFavoriteList: Boolean = false,
    viewModel: MarketListViewModel = hiltViewModel(key = if (showFavoriteList) "favorites" else "markets"),
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)
    LaunchedEffect(key1 = Unit) {
        event.invoke(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = showFavoriteList))
    }
    if (showFavoriteList) {
        FavoriteMarketListScreen(
            state = state,
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            onFavoriteClick = { market ->
                event.invoke(MarketListContract.Event.OnFavoriteClick(market = market))
            },
            onRefresh = {
                event.invoke(MarketListContract.Event.OnGetMarketList)
            },
        )
    } else {
        val lazyPagingItems = viewModel.pagedMarketList.collectAsLazyPagingItems()
        PagedMarketListScreen(
            lazyPagingItems = lazyPagingItems,
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            onFavoriteClick = { market ->
                event.invoke(MarketListContract.Event.OnFavoriteClick(market = market))
            },
        )
    }
}

@Composable
fun PagedMarketListScreen(
    lazyPagingItems: LazyPagingItems<MarketModel>,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
) {
    val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { lazyPagingItems.refresh() },
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(refreshState),
    ) {
        PullRefreshIndicator(
            isRefreshing,
            refreshState,
            Modifier.align(Alignment.TopCenter),
        )
        when (val refreshLoadState = lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {
                ErrorView(errorMessage = refreshLoadState.error.message ?: "Unknown error")
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.name },
                    ) { index ->
                        val market = lazyPagingItems[index]
                        if (market != null) {
                            Column(
                                modifier = Modifier.animateItem(
                                    placementSpec = tween(durationMillis = 250),
                                    fadeInSpec = null,
                                    fadeOutSpec = null,
                                ),
                            ) {
                                MarketListItem(
                                    modifier = Modifier,
                                    market = market,
                                    showFavoriteList = false,
                                    onItemClick = { onNavigateToDetailScreen(market) },
                                    onFavoriteClick = { onFavoriteClick(market) },
                                )
                            }
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
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
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
    }
}

@Composable
fun FavoriteMarketListScreen(
    state: MarketListContract.State,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: () -> Unit,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = state.favoriteMarketList.isLoading,
        onRefresh = onRefresh,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(refreshState),
    ) {
        PullRefreshIndicator(
            state.favoriteMarketList.isLoading,
            refreshState,
            Modifier.align(Alignment.TopCenter),
        )
        LoadableComponent(
            loadableData = state.favoriteMarketList,
            loaded = { data ->
                AnimatedVisibility(
                    visible = !state.favoriteMarketList.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    if (data.isEmpty() && state.showFavoriteList) {
                        EmptyStateAnimation(
                            lottieCompositionSpec = LottieCompositionSpec.RawRes(
                                R.raw.empty_state_animation,
                            ),
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(
                                items = data,
                                key = { it.name },
                            ) { market ->
                                Column(
                                    modifier = Modifier.animateItem(
                                        placementSpec = tween(durationMillis = 250),
                                        fadeInSpec = null,
                                        fadeOutSpec = null,
                                    ),
                                ) {
                                    MarketListItem(
                                        modifier = Modifier,
                                        market = market,
                                        showFavoriteList = true,
                                        onItemClick = { onNavigateToDetailScreen(market) },
                                        onFavoriteClick = { onFavoriteClick(market) },
                                    )
                                }
                            }
                        }
                    }
                }
            },
            error = { error ->
                ErrorView(errorMessage = errorViewMapper(error))
            },
        )
    }
}

@ThemePreviews
@Composable
private fun MarketListScreenPrev(
    @PreviewParameter(MarketListStateProvider::class)
    marketListState: MarketListContract.State,
) {
    ComposeNewsTheme {
        Surface {
            FavoriteMarketListScreen(
                state = marketListState,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
    }
}
