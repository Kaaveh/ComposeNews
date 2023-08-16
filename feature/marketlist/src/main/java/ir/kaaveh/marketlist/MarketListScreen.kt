package ir.kaaveh.marketlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kaaveh.base.BaseRoute
import ir.kaaveh.base.BaseViewModel
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.kaaveh.base.use
import ir.kaaveh.domain.model.Market
import ir.kaaveh.marketlist.component.MarketListItem
import ir.kaaveh.marketlist.preview_provider.MarketListStateProvider

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

@OptIn(ExperimentalMaterialApi::class)
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
                    MarketListItem(
                        modifier = Modifier,
                        market = market,
                        onItemClick = {
                            onNavigateToDetailScreen(market)
                        },
                        onFavoriteClick = {
                            onFavoriteClick(market)
                        }
                    )
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
    marketListState: MarketListContract.State
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