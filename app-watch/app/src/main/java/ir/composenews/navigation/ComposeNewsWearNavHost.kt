package ir.composenews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import ir.composenews.app_watch.navigation.graph.MainContract
import ir.composenews.app_watch.navigation.graph.Destinations
import ir.composenews.app_watch.navigation.graph.wearMarketDetail
import ir.composenews.app_watch.navigation.graph.wearMarketList
import ir.composenews.uimarket.model.MarketModel

@Composable
fun ComposeNewsWearNavHost(
    navController: NavHostController,
    modifier: Modifier,
    onMarketSelected: ((MarketModel) -> Unit)? = null,
    uiState: MainContract.State
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Destinations.MarketListScreen.route,
        modifier = modifier,
    ) {
        wearMarketList(
            showFavorite = false,
            onMarketSelected = onMarketSelected,
        )
        wearMarketDetail(uiState)
    }
}