package ir.kaaveh.composenews.navigation.graph

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kaaveh.composenews.navigation.Destinations
import ir.kaaveh.composenews.navigation.navigate
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.favoritenews.FavoriteNewsRoute

fun NavGraphBuilder.favoriteNews(
    navController: NavController,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    composable(Destinations.FavoriteNewsScreen.route) {
        FavoriteNewsRoute(
            onNavigateToDetailScreen = { news ->
                navController.navigate(
                    route = Destinations.NewsDetailScreen().route,
                    args = bundleOf(Destinations.NewsDetailScreen().news to news)
                )
            },
            onProvideBaseViewModel = {
                onProvideBaseViewModel(it)
            },
        )
    }
    newsDetail(onProvideBaseViewModel)
}