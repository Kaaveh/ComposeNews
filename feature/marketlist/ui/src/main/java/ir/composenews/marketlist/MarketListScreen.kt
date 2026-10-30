@file:Suppress(
    "MaxLineLength",
    "ktlint",
    "UnusedPrivateMember",
    "LongMethod",
    "LongParameterList",
)

package ir.composenews.marketlist

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.koin.compose.viewmodel.koinViewModel
import ir.composenews.base.LoadableData
import ir.composenews.core.ui.LoadableComponent
import ir.composenews.base.errorViewMapper
import ir.composenews.base.isLoading
import ir.composenews.core.ui.use
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
import kotlinx.collections.immutable.PersistentList

@Composable
fun MarketListRoute(
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    if (showFavoriteList) {
        FavoriteMarketListRoute(onNavigateToDetailScreen = onNavigateToDetailScreen)
    } else {
        val (state, event) = use(viewModel = koinViewModel<MarketListViewModel>())
        LaunchedEffect(key1 = Unit) {
            event.invoke(MarketListContract.Event.OnGetMarketList)
        }
        MarketListScreen(
            state = state,
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            onFavoriteClick = { market ->
                event.invoke(MarketListContract.Event.OnFavoriteClick(market = market))
            },
            onRefresh = {
                event.invoke(MarketListContract.Event.OnGetMarketList)
            },
        )
    }
}

@Composable
private fun FavoriteMarketListRoute(
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    val (state, event) = use(viewModel = koinViewModel<FavoriteListViewModel>())
    LaunchedEffect(key1 = Unit) {
        event.invoke(FavoriteListContract.Event.OnGetFavoriteList)
    }
    FavoriteMarketListScreen(
        state = state,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = { market ->
            event.invoke(FavoriteListContract.Event.OnFavoriteClick(market = market))
        },
    )
}

@Composable
fun MarketListScreen(
    state: MarketListContract.State,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: () -> Unit,
) {
    // Report the app as fully drawn once market content is ready so
    // startup benchmarks can measure TTFD (Time To Full Display).
    ReportDrawnWhen {
        state.marketList is LoadableData.Loaded || state.marketList is LoadableData.Error
    }
    MarketListContent(
        marketList = state.marketList,
        isRefreshing = state.isRefreshing,
        showFavoriteList = false,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = onFavoriteClick,
        onRefresh = onRefresh,
    )
}

@Composable
fun FavoriteMarketListScreen(
    state: FavoriteListContract.State,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
) {
    MarketListContent(
        marketList = state.favoriteList,
        isRefreshing = state.favoriteList.isLoading,
        showFavoriteList = true,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = onFavoriteClick,
        onRefresh = null,
    )
}

/** [onRefresh] null turns pull to refresh off. */
@Composable
private fun MarketListContent(
    marketList: LoadableData<PersistentList<MarketModel>>,
    isRefreshing: Boolean,
    showFavoriteList: Boolean,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: (() -> Unit)?,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { onRefresh?.invoke() },
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onRefresh != null) Modifier.pullRefresh(refreshState) else Modifier),
    ) {
        if (onRefresh != null) {
            PullRefreshIndicator(
                isRefreshing,
                refreshState,
                Modifier.align(Alignment.TopCenter),
            )
        }
        LoadableComponent(
            loadableData = marketList,
            loaded = { data ->
                AnimatedVisibility(
                    visible = !marketList.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    if (data.isEmpty() && showFavoriteList) {
                        EmptyStateAnimation()
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(
                                items = data,
                                key = { it.id },
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
                                        showFavoriteList = showFavoriteList,
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
            MarketListScreen(
                state = marketListState,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
    }
}
