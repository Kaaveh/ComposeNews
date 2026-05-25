@file:Suppress("ktlint")

package ir.composenews.apptv.navigation.graph

import androidx.compose.runtime.Composable
import ir.composenews.apptv.ui.list.MarketListTvRoute
import ir.composenews.uimarket.model.MarketModel

@Composable
fun TvMarketListEntry(
    showFavoriteList: Boolean,
    onMarketSelected: (MarketModel) -> Unit,
) {
    MarketListTvRoute(
        showFavoriteList = showFavoriteList,
        onNavigateToDetailScreen = onMarketSelected,
    )
}
