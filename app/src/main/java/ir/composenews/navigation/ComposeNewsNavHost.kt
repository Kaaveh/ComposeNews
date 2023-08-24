package ir.composenews.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ir.composenews.navigation.graph.favoriteMarketList
import ir.composenews.navigation.graph.marketDetail
import ir.composenews.navigation.graph.marketList
import ir.composenews.base.BaseViewModel

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
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        marketList(navController, onProvideBaseViewModel)
        favoriteMarketList(navController, onProvideBaseViewModel)
        marketDetail(onProvideBaseViewModel)
    }
}