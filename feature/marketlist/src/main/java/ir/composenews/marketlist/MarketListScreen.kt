@file:Suppress("MaxLineLength", "ComplexCondition", "ktlint:standard:function-naming")

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
import com.airbnb.lottie.compose.LottieCompositionSpec
import ir.composenews.base.BaseRoute
import ir.composenews.base.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.EmptyStateAnimation
import ir.composenews.designsystem.component.ShimmerMarketListItem
import ir.composenews.designsystem.component.pull_refresh_indicator.PullRefreshIndicator
import ir.composenews.designsystem.component.pull_refresh_indicator.pullRefresh
import ir.composenews.designsystem.component.pull_refresh_indicator.rememberPullRefreshState
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.marketlist.preview_provider.MarketListStateProvider
import ir.composenews.uimarket.model.MarketModel
import ir.composenews.utils.ContentType

/**
 * LongParameterList - > compose unimited
 */
@Composable
fun MarketListRoute(
    viewModel: MarketListViewModel = hiltViewModel(),
    showFavoriteList: Boolean = false,
    isDetailOnlyOpen: Boolean,
    marketModel: MarketModel?,
    closeDetailScreen: () -> Unit,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    contentType: ContentType,
) {
    val (state, event) = use(viewModel = viewModel)
    LaunchedEffect(key1 = Unit) {
        event.invoke(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = showFavoriteList))
        if (!showFavoriteList) {
            event.invoke(MarketListContract.Event.OnGetMarketList)
        }
    }

    LaunchedEffect(key1 = contentType) {
        if (contentType == ContentType.SINGLE_PANE && !isDetailOnlyOpen) {
            closeDetailScreen()
        }
    }

    if (contentType == ContentType.DUAL_PANE && !state.refreshing && state.marketList.isNotEmpty() && marketModel == null) {
        onNavigateToDetailScreen(state.marketList[0])
    }

    BaseRoute(
        baseViewModel = viewModel,
        shimmerView = {
            ShimmerMarketListItem()
        },
    ) {
        MarketListScreen(
            marketListState = state,
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            showFavoriteList = showFavoriteList,
            onFavoriteClick = { market ->
                event.invoke(MarketListContract.Event.OnFavoriteClick(market = market))
            },
            onRefresh = {
                event.invoke(MarketListContract.Event.OnRefresh)
            },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MarketListScreen(
    marketListState: MarketListContract.State,
    showFavoriteList: Boolean,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: () -> Unit,
) {
    val refreshState =
        rememberPullRefreshState(refreshing = marketListState.refreshing, onRefresh = onRefresh)

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .pullRefresh(refreshState),
    ) {
        AnimatedVisibility(
            visible = !marketListState.refreshing,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (marketListState.showFavoriteEmptyState && marketListState.showFavoriteList) {
                EmptyStateAnimation(
                    lottieCompositionSpec =
                    LottieCompositionSpec.RawRes(
                        R.raw.empty_state_animation,
                    ),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(
                        items = marketListState.marketList,
                        key = { it.name },
                    ) { market ->
                        Column(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(
                                    animationSpec = tween(durationMillis = 250),
                                ),
                        ) {
                            MarketListItem(
                                modifier = Modifier,
                                market = market,
                                showFavoriteList = showFavoriteList,
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
        }
        PullRefreshIndicator(
            marketListState.refreshing,
            refreshState,
            Modifier.align(Alignment.TopCenter),
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
                showFavoriteList = false,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
    }
}

@Composable
fun TestableMarketListScreen(
    marketListState: MarketListContract.State,
    showFavoriteList: Boolean,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: () -> Unit,
) {
    MarketListScreen(
        marketListState = marketListState,
        showFavoriteList = showFavoriteList,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        onFavoriteClick = onFavoriteClick,
        onRefresh = onRefresh,
    )
}
