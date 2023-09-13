package ir.composenews.marketlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.hilt.navigation.compose.hiltViewModel
import ir.composenews.base.BaseRoute
import ir.composenews.base.BaseViewModel
import ir.composenews.base.use
import ir.composenews.designsystem.component.pull_refresh_indicator.PullRefreshIndicator
import ir.composenews.designsystem.component.pull_refresh_indicator.pullRefresh
import ir.composenews.designsystem.component.pull_refresh_indicator.rememberPullRefreshState
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.domain.model.Market
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.marketlist.preview_provider.MarketListStateProvider

@Composable
fun MarketListRoute(
    viewModel: MarketListViewModel = hiltViewModel(),
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (market: Market) -> Unit,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)

    LaunchedEffect(key1 = Unit) {
        onProvideBaseViewModel(viewModel)
        event.invoke(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = showFavoriteList))
        event.invoke(MarketListContract.Event.OnGetMarketList)
    }

    BaseRoute(baseViewModel = viewModel) {
        MarketListScreen(
            marketListState = state,
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            onFavoriteClick = { market ->
                event.invoke(MarketListContract.Event.OnFavoriteClick(market = market))
            },
            onRefresh = {
                event.invoke(MarketListContract.Event.OnRefresh)
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MarketListScreen(
    marketListState: MarketListContract.State,
    onNavigateToDetailScreen: (market: Market) -> Unit,
    onFavoriteClick: (market: Market) -> Unit,
    onRefresh: () -> Unit,
) {
    val refreshState =
        rememberPullRefreshState(refreshing = marketListState.refreshing, onRefresh = onRefresh)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(refreshState)
    ) {
        AnimatedVisibility(
            visible = !marketListState.refreshing,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = marketListState.marketList,
                    key = { it.name },
                ) { market ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(
                                animationSpec = tween(durationMillis = 250)
                            )
                    ) {
                        MarketListItem(
                            modifier = Modifier,
                            market = market,
                            onItemClick = {
                                onNavigateToDetailScreen(market)
                            },
                            onFavoriteClick = {
                                onFavoriteClick(market)
                            },
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(
            marketListState.refreshing,
            refreshState,
            Modifier.align(Alignment.TopCenter)
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
                marketListState = marketListState,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
    }
}