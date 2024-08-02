package ir.composenews.app_watch.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import ir.composenews.app_watch.ui.list.MarketListWearRoute
import ir.composenews.uimarket.model.MarketModel

fun NavGraphBuilder.wearMarketList(
    showFavorite: Boolean,
    onMarketSelected: ((MarketModel) -> Unit)? = null,
) {
    composable(Destinations.MarketListScreen.route) {
        MarketListWearRoute(
            onNavigateToDetailScreen = { market ->
                onMarketSelected?.invoke(market)
            },
            showFavoriteList = showFavorite,
        )
    }
}