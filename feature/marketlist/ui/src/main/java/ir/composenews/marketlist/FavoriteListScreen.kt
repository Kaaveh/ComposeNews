@file:Suppress(
    "MaxLineLength",
    "ktlint",
    "UnusedPrivateMember",
    "LongMethod",
    "LongParameterList",
    "MagicNumber",
)

package ir.composenews.marketlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ir.composenews.base.LoadableData
import ir.composenews.base.errorViewMapper
import ir.composenews.core.ui.marketList
import ir.composenews.core.ui.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.DismissBackgroundSwipe
import ir.composenews.designsystem.component.EmptyFavoritesPane
import ir.composenews.designsystem.component.SortMenuButton
import ir.composenews.designsystem.component.StatePaneButton
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.groupedRowShape
import ir.composenews.designsystem.widget.ErrorView
import ir.composenews.designsystem.widget.LoadingView
import ir.composenews.marketlist.FavoriteListContract.Sort
import ir.composenews.marketlist.component.ListTitle
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.uimarket.mapper.toMarketModel
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

/** [selectedMarketId]: the market the detail beside the list shows. */
@Composable
fun FavoriteListRoute(
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onBrowseMarkets: () -> Unit,
    selectedMarketId: String? = null,
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
        onSortSelect = { event.invoke(FavoriteListContract.Event.OnSortSelect(it)) },
        onBrowseMarkets = onBrowseMarkets,
        selectedMarketId = selectedMarketId,
    )
}

@Composable
fun FavoriteMarketListScreen(
    state: FavoriteListContract.State,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
    onSortSelect: (Sort) -> Unit = {},
    onBrowseMarkets: () -> Unit = {},
    selectedMarketId: String? = null,
) {
    when (val favorites = state.favoriteList) {
        is LoadableData.Loaded -> if (favorites.data.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                ListTitle(title = "Favorites")
                EmptyFavoritesPane(modifier = Modifier.weight(1f)) {
                    StatePaneButton(
                        text = "Browse markets",
                        icon = painterResource(R.drawable.ic_finance_mode),
                        onClick = onBrowseMarkets,
                        shape = CircleShape,
                    )
                }
            }
        } else {
            FavoriteListContent(
                state = state,
                favorites = favorites.data,
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                onRemove = onFavoriteClick,
                onSortSelect = onSortSelect,
                selectedMarketId = selectedMarketId,
            )
        }

        // Room doesn't fail here today; kept so a future error isn't a blank screen.
        is LoadableData.Error -> ErrorView(errorMessage = errorViewMapper(favorites.error))
        LoadableData.Loading, LoadableData.Initial -> LoadingView(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun FavoriteListContent(
    state: FavoriteListContract.State,
    favorites: PersistentList<MarketModel>,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onRemove: (market: MarketModel) -> Unit,
    onSortSelect: (Sort) -> Unit,
    selectedMarketId: String?,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        item(key = "title") {
            ListTitle(
                title = "Favorites",
                subtitle = "${state.count} saved · swipe left to remove",
                modifier = Modifier.padding(bottom = 14.dp),
            ) {
                SortMenuButton(
                    options = Sort.entries,
                    selected = state.sort,
                    onSelect = onSortSelect,
                    label = {
                        when (it) {
                            Sort.Name -> "Name"
                            Sort.Price -> "Price"
                            Sort.Change24h -> "24h change"
                        }
                    },
                    contentDescription = "Sort favorites by name, price or 24h change",
                )
            }
        }
        itemsIndexed(items = favorites, key = { _, market -> "market_${market.id}" }) { index, market ->
            SwipeToRemoveRow(
                market = market,
                shape = groupedRowShape(index, favorites.size),
                onClick = { onNavigateToDetailScreen(market) },
                onRemove = { onRemove(market) },
                selected = market.id == selectedMarketId,
                modifier = Modifier.animateItem(),
            )
        }
        item(key = "footer") {
            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(24.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_notifications_active),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = "Favorites stay in sync in the background, even offline.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

/** A swipe to the left past [SWIPE_THRESHOLD] removes the market; the list fades the row out. */
@Composable
private fun SwipeToRemoveRow(
    market: MarketModel,
    shape: Shape,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val threshold = with(LocalDensity.current) { SWIPE_THRESHOLD.toPx() }
    // Not saveable: the list keeps saved state per key, so a market favorited again would come back
    // already swiped and be removed again at once.
    val dismissState = remember { SwipeToDismissBoxState(SwipeToDismissBoxValue.Settled, positionalThreshold = { threshold }) }
    // SwipeToDismissBox calls onDismiss again whenever the lambda changes while the row sits dismissed.
    var removed by remember { mutableStateOf(false) }
    SwipeToDismissBox(
        state = dismissState,
        // The swipe background mustn't show through the row's rounded corners.
        modifier = modifier.clip(shape),
        enableDismissFromStartToEnd = false,
        onDismiss = {
            if (it == SwipeToDismissBoxValue.EndToStart && !removed) {
                removed = true
                onRemove()
            }
        },
        backgroundContent = { DismissBackgroundSwipe(modifier = Modifier, dismissState = dismissState, shape = shape) },
    ) {
        MarketListItem(
            market = market,
            shape = shape,
            onItemClick = onClick,
            onFavoriteClick = null,
            onRemove = onRemove,
            selected = selected,
        )
    }
}

private val SWIPE_THRESHOLD = 170.dp

@ThemePreviews
@Composable
private fun FavoriteMarketListScreenPrev() {
    ComposeNewsTheme {
        Surface {
            FavoriteMarketListScreen(
                state = FavoriteListContract.State(
                    favoriteList = LoadableData.Loaded(
                        marketList.take(3).map { it.toMarketModel().copy(id = it.name, isFavorite = true) }.toPersistentList(),
                    ),
                ),
                onNavigateToDetailScreen = {},
                onFavoriteClick = {},
            )
        }
    }
}
