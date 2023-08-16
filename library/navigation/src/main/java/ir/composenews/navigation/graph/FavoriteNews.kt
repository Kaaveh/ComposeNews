package ir.composenews.navigation.graph

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.composenews.base.BaseViewModel
import ir.kaaveh.marketlist.MarketListRoute
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.extension_function.navigate

fun NavGraphBuilder.favoriteMarketList(
    navController: NavController,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    composable(Destinations.FavoriteMarketScreen.route) {
        MarketListRoute(
            showFavoriteList = true,
            onNavigateToDetailScreen = { market ->
                navController.navigate(
                    route = Destinations.MarketDetailScreen().route,
                    args = bundleOf(Destinations.MarketDetailScreen().market to market)
                )
            },
            onProvideBaseViewModel = {
                onProvideBaseViewModel(it)
            },
        )
    }
}