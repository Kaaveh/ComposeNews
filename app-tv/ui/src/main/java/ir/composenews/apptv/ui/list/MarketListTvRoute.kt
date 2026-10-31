@file:Suppress("ktlint")

package ir.composenews.apptv.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.unit.dp
import ir.composenews.base.LoadableData
import ir.composenews.core.ui.LoadableComponent
import ir.composenews.base.errorViewMapper
import ir.composenews.core.ui.use
import ir.composenews.designsystem.component.EmptyFavoritesPane
import ir.composenews.designsystem.widget.ErrorView
import ir.composenews.marketlist.FavoriteListContract
import ir.composenews.marketlist.FavoriteListViewModel
import ir.composenews.marketlist.MarketListContract
import ir.composenews.marketlist.MarketListViewModel
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.PersistentList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MarketListTvRoute(
    showFavoriteList: Boolean = false,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 24.dp),
    ) {
        if (showFavoriteList) {
            val (state, event) = use(viewModel = koinViewModel<FavoriteListViewModel>())
            LaunchedEffect(Unit) {
                event(FavoriteListContract.Event.OnGetFavoriteList)
            }
            MarketListTvScreen(
                marketList = state.favoriteList,
                showFavoriteList = true,
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                onFavoriteClick = { market ->
                    event(FavoriteListContract.Event.OnFavoriteClick(market = market))
                },
            )
        } else {
            val (state, event) = use(viewModel = koinViewModel<MarketListViewModel>())
            LaunchedEffect(Unit) {
                event(MarketListContract.Event.OnGetMarketList)
            }
            MarketListTvScreen(
                marketList = state.marketList,
                showFavoriteList = false,
                onNavigateToDetailScreen = onNavigateToDetailScreen,
                onFavoriteClick = { market ->
                    event(MarketListContract.Event.OnFavoriteClick(market = market))
                },
            )
        }
    }
}

@Composable
private fun MarketListTvScreen(
    marketList: LoadableData<PersistentList<MarketModel>>,
    showFavoriteList: Boolean,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
    onFavoriteClick: (market: MarketModel) -> Unit,
) {
    LoadableComponent(
        loadableData = marketList,
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        loaded = { data ->
            if (data.isEmpty() && showFavoriteList) {
                EmptyFavoritesPane()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(items = data, key = { _, m -> m.id }) { index, market ->
                        val isFirst = index == 0
                        val isLast = index == data.lastIndex
                        MarketListItem(
                            modifier = focusEdgeModifier(isFirst = isFirst, isLast = isLast),
                            market = market,
                            shape = RoundedCornerShape(24.dp),
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
