package ir.composenews.appwatch.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import ir.composenews.appwatch.navigation.MainContract
import ir.composenews.appwatch.ui.details.MarketDetailWearRoute

fun NavGraphBuilder.wearMarketDetail(uiState: MainContract.State) {
    composable(
        route = Destinations.MarketDetailScreen().route,
    ) {
        uiState.market?.let { market ->
            MarketDetailWearRoute(market = market)
        }
    }
}
