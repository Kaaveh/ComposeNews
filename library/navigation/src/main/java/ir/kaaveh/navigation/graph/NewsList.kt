package ir.kaaveh.navigation.graph

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kaaveh.navigation.Destinations
import ir.kaaveh.navigation.extension_function.navigate
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.marketlist.MarketListRoute

fun NavGraphBuilder.marketList(
    navController: NavController,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    composable(Destinations.MarketListScreen.route) {
        MarketListRoute(
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