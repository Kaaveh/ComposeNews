package ir.kaaveh.navigation.graph

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.navigation.Destinations
import ir.kaaveh.navigation.extension_function.navigate
import ir.kaaveh.newslist.NewsListRoute

fun NavGraphBuilder.favoriteNews(
    navController: NavController,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    composable(Destinations.FavoriteNewsScreen.route) {
        NewsListRoute(
            showFavoriteList = true,
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
}