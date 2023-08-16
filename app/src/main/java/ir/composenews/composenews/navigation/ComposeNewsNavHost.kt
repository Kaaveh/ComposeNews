package ir.composenews.composenews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ir.composenews.navigation.graph.favoriteMarketList
import ir.composenews.navigation.graph.marketDetail
import ir.composenews.navigation.graph.marketList
import ir.composenews.base.BaseViewModel
import ir.composenews.navigation.Destinations

@Composable
fun ComposeNewsNavHost(
    navController: NavHostController,
    modifier: Modifier,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.MarketListScreen.route,
        modifier = modifier,
    ) {
        marketList(navController, onProvideBaseViewModel)
        favoriteMarketList(navController, onProvideBaseViewModel)
        marketDetail(onProvideBaseViewModel)
    }
}