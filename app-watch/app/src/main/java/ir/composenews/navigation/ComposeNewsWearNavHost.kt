package ir.composenews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import ir.composenews.appwatch.navigation.MainContract
import ir.composenews.appwatch.navigation.graph.Destinations
import ir.composenews.appwatch.navigation.graph.wearMarketDetail
import ir.composenews.appwatch.navigation.graph.wearMarketList
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