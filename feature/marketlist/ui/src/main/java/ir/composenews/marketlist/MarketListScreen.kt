@file:Suppress(
    "MaxLineLength",
    "ktlint",
    "UnusedPrivateMember",
    "LongMethod",
    "LongParameterList",
    "MagicNumber",
)

package ir.composenews.marketlist

import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ir.composenews.base.LoadableData
import ir.composenews.base.errorViewMapper
import ir.composenews.core.ui.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.ConnectedButtonGroup
import ir.composenews.designsystem.component.SortMenuButton
import ir.composenews.designsystem.component.StatePane
import ir.composenews.designsystem.component.StatePaneButton
import ir.composenews.designsystem.component.TopMoverCard
import ir.composenews.designsystem.component.formatUsdPrice
import ir.composenews.designsystem.component.SkeletonBlock
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.groupedRowShape
import ir.composenews.marketlist.MarketListContract.Filter
import ir.composenews.marketlist.MarketListContract.Header
import ir.composenews.marketlist.MarketListContract.Sort
import ir.composenews.marketlist.component.ListTitle
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.marketlist.preview_provider.MarketListStateProvider
import ir.composenews.network.Errors
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList
import java.util.Locale

private const val SKELETON_ROWS = 6

/** [inPane]: the list sits beside the detail, which then shows [selectedMarketId]. */
@Composable
fun MarketListRoute(
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onOpenFavorites: () -> Unit,
    selectedMarketId: String? = null,
    inPane: Boolean = false,
) {
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
            event.invoke(MarketListContract.Event.OnRefresh)
        },
        onFilterSelect = { event.invoke(MarketListContract.Event.OnFilterSelect(it)) },
        onSortSelect = { event.invoke(MarketListContract.Event.OnSortSelect(it)) },
        onOpenFavorites = onOpenFavorites,
        onSyncErrorShown = { event.invoke(MarketListContract.Event.OnSyncErrorShown) },
        selectedMarketId = selectedMarketId,
        inPane = inPane,
    )
}

@Composable
fun MarketListScreen(
    state: MarketListContract.State,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onRefresh: () -> Unit,
    onFilterSelect: (Filter) -> Unit = {},
    onSortSelect: (Sort) -> Unit = {},
    onOpenFavorites: () -> Unit = {},
    onSyncErrorShown: () -> Unit = {},
    selectedMarketId: String? = null,
    inPane: Boolean = false,
) {
    // Report the app as fully drawn once market content is ready so
    // startup benchmarks can measure TTFD (Time To Full Display).
    ReportDrawnWhen {
        state.marketList is LoadableData.Loaded || state.marketList is LoadableData.Error
    }
    val snackbarHostState = remember { SnackbarHostState() }
    state.syncError?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(errorViewMapper(error))
            onSyncErrorShown()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (val marketList = state.marketList) {
            is LoadableData.Loaded -> PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                MarketListContent(
                    state = state,
                    markets = marketList.data,
                    onNavigateToDetailScreen = onNavigateToDetailScreen,
                    onFavoriteClick = onFavoriteClick,
                    onFilterSelect = onFilterSelect,
                    onSortSelect = onSortSelect,
                    selectedMarketId = selectedMarketId,
                    inPane = inPane,
                )
            }

            is LoadableData.Error -> Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                ListTitle(title = "Markets")
                MarketErrorPane(
                    error = marketList.error,
                    retryInSeconds = state.retryInSeconds,
                    onRefresh = onRefresh,
                    onOpenFavorites = onOpenFavorites,
                    modifier = Modifier.weight(1f),
                )
            }

            LoadableData.Loading, LoadableData.Initial -> MarketListSkeleton()
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun MarketListContent(
    state: MarketListContract.State,
    markets: PersistentList<MarketModel>,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onFilterSelect: (Filter) -> Unit,
    onSortSelect: (Sort) -> Unit,
    selectedMarketId: String?,
    inPane: Boolean,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        item(key = "title") {
            ListTitle(title = "Markets") {
                SortMenuButton(
                    options = Sort.entries,
                    selected = state.sort,
                    onSelect = onSortSelect,
                    label = { if (it == Sort.MarketCap) "Market cap" else "Volume" },
                    icon = { painterResource(if (it == Sort.MarketCap) R.drawable.ic_pie_chart else R.drawable.ic_bar_chart) },
                )
            }
        }
        // Beside the detail the list is narrow; the top mover is one tap away in it anyway.
        state.topMover?.takeIf { !inPane }?.let { mover ->
            item(key = "topMover") {
                TopMoverCard(
                    id = mover.id,
                    name = mover.name,
                    symbol = mover.symbol,
                    imageUrl = mover.imageUrl,
                    price = formatUsdPrice(mover.currentPrice),
                    change = mover.priceChangePercentage24h,
                    sparkline = mover.sparkline7d,
                    onClick = { onNavigateToDetailScreen(mover) },
                    modifier = Modifier.padding(top = 14.dp),
                )
            }
        }
        item(key = "filter") {
            ConnectedButtonGroup(
                options = Filter.entries,
                selected = state.filter,
                onSelect = onFilterSelect,
                label = { it.name },
                icon = {
                    painterResource(
                        when (it) {
                            Filter.All -> R.drawable.ic_apps
                            Filter.Gainers -> R.drawable.ic_trending_up
                            Filter.Losers -> R.drawable.ic_trending_down
                        },
                    )
                },
                modifier = Modifier.padding(top = 14.dp),
            )
        }
        item(key = "header") {
            Text(
                text = state.header.label,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 4.dp, top = 14.dp, end = 4.dp, bottom = 4.dp),
            )
        }
        if (markets.isEmpty()) {
            item(key = "empty") {
                Text(
                    text = "No markets match this filter",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }
        itemsIndexed(items = markets, key = { _, market -> "market_${market.id}" }) { index, market ->
            MarketListItem(
                market = market,
                shape = groupedRowShape(index, markets.size),
                onItemClick = { onNavigateToDetailScreen(market) },
                // In a pane the detail header has the favorite toggle.
                onFavoriteClick = if (inPane) null else ({ onFavoriteClick(market) }),
                selected = market.id == selectedMarketId,
                modifier = Modifier.animateItem(
                    placementSpec = tween(durationMillis = 250),
                    fadeInSpec = null,
                    fadeOutSpec = null,
                ),
            )
        }
    }
}

private val Header.label: String
    get() = when (this) {
        Header.ByMarketCap -> "Top 250 · By market cap"
        Header.ByVolume -> "Top 250 · By volume"
        Header.Gainers -> "Top 250 gainers · 24h"
        Header.Losers -> "Top 250 losers · 24h"
    }

/** Nothing stored and the sync failed: a plain error, or a 429 whose `Try again` waits out the countdown. */
@Composable
private fun MarketErrorPane(
    error: Errors,
    retryInSeconds: Int,
    onRefresh: () -> Unit,
    onOpenFavorites: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rateLimited = error is Errors.RateLimited
    val waiting = retryInSeconds > 0
    StatePane(
        icon = painterResource(if (rateLimited) R.drawable.ic_hourglass_top else R.drawable.ic_cloud_off),
        iconTint = if (rateLimited) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        containerColor = if (rateLimited) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer,
        title = errorViewMapper(error),
        body = when {
            !rateLimited -> "Prices couldn’t load. Check your connection and try again."
            waiting -> "You can try again when the timer ends."
            else -> "You can try again now."
        },
        modifier = modifier,
    ) {
        StatePaneButton(
            text = if (waiting) {
                String.format(Locale.US, "Try again in %d:%02d", retryInSeconds / 60, retryInSeconds % 60)
            } else {
                "Try again"
            },
            icon = painterResource(if (waiting) R.drawable.ic_timer else R.drawable.ic_refresh),
            onClick = onRefresh,
            enabled = !waiting,
        )
        TextButton(onClick = onOpenFavorites, modifier = Modifier.heightIn(min = 48.dp)) {
            Text(text = "Open saved favorites")
        }
    }
}

/** The first load: the title with a loader in place of the sort button, then placeholders for the card, filters and rows. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MarketListSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ListTitle(title = "Markets") {
            ContainedLoadingIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .semantics { contentDescription = "Loading markets" },
            )
        }
        SkeletonBlock(shape = RoundedCornerShape(32.dp), modifier = Modifier.fillMaxWidth().height(176.dp))
        SkeletonBlock(shape = CircleShape, modifier = Modifier.fillMaxWidth().height(48.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(SKELETON_ROWS) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(groupedRowShape(index, SKELETON_ROWS))
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(start = 12.dp, top = 14.dp, end = 16.dp, bottom = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SkeletonBlock(shape = CircleShape, modifier = Modifier.size(48.dp))
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonBlock(modifier = Modifier.size(64.dp, 16.dp))
                        SkeletonBlock(modifier = Modifier.size(96.dp, 12.dp))
                    }
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SkeletonBlock(modifier = Modifier.size(84.dp, 16.dp))
                        SkeletonBlock(modifier = Modifier.size(52.dp, 12.dp))
                    }
                }
            }
        }
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
