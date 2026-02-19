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
    viewModel: MarketListViewModel = hiltViewModel(),
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    val (state, event) = use(viewModel = viewModel)
    LaunchedEffect(key1 = Unit) {
        event.invoke(MarketListContract.Event.OnSetShowFavoriteList(showFavoriteList = showFavoriteList))
    }
    MarketListScreen(
        state = state,
        onNavigateToDetailScreen = onNavigateToDetailScreen,
        showFavoriteList = showFavoriteList,
        onFavoriteClick = { market ->
            event.invoke(MarketListContract.Event.OnFavoriteClick(market = market))
        },
        onRefresh = {
            event.invoke(MarketListContract.Event.OnGetMarketList)
        },
    )
}

@Composable
fun MarketListScreen(
    state: MarketListContract.State,
    showFavoriteList: Boolean,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: () -> Unit,
) {
    val refreshState = rememberPullRefreshState(
        refreshing = state.marketList.isLoading,
        onRefresh = onRefresh,
    )
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .pullRefresh(refreshState),
    ) {
        PullRefreshIndicator(
            state.marketList.isLoading,
            refreshState,
            Modifier.align(Alignment.TopCenter),
        )
        LoadableComponent(
            loadableData = state.marketList,
            loaded = { data ->
                AnimatedVisibility(
                    visible = !state.marketList.isLoading,
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
                                Modifier
                                    .fillMaxWidth()
                                Column(
                                    modifier =
                                    Modifier.animateItem(
                                        placementSpec = tween(durationMillis = 250),
                                        fadeInSpec = null,
                                        fadeOutSpec = null,
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
                showFavoriteList = false,
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
                onRefresh = {},
            )
        }
    }
}
