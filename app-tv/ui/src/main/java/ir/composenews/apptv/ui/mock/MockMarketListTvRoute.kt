@file:Suppress("ktlint")

package ir.composenews.apptv.ui.mock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.composenews.marketlist.component.MarketListItem
import ir.composenews.uimarket.model.MarketModel

@Composable
fun MockMarketListTvRoute(
    showFavoriteList: Boolean,
    onNavigateToDetailScreen: (market: MarketModel) -> Unit,
) {
    val data = remember(showFavoriteList) {
        if (showFavoriteList) fakeFavoriteMarkets else fakeMarkets
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = data, key = { it.id }) { market ->
            MarketListItem(
                modifier = Modifier,
                market = market,
                showFavoriteList = showFavoriteList,
                onItemClick = { onNavigateToDetailScreen(market) },
                onFavoriteClick = {},
            )
        }
    }
}
