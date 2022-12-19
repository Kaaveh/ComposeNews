package ir.kaaveh.composenews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.kaaveh.domain.model.News
import ir.kaaveh.favoritenews.FavoriteNewsRoute
import ir.kaaveh.newsdetail.NewsDetailRoute
import ir.kaaveh.newslist.NewsListRoute

@Composable
fun ComposeNewsNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Destinations.NewsListScreen.route,
        modifier = modifier
    ) {
        composable(Destinations.NewsListScreen.route) {
            NewsListRoute(
                onNavigateToDetailScreen = { news ->
                    navController.navigate(
                        route = Destinations.NewsDetailScreen().route,
                        args = bundleOf(Destinations.NewsDetailScreen().news to news)
                    )
                }
            )
        }
        composable(Destinations.FavoriteNewsScreen.route) {
            FavoriteNewsRoute(
                onNavigateToDetailScreen = { news ->
                    navController.navigate(
                        route = Destinations.NewsDetailScreen().route,
                        args = bundleOf(Destinations.NewsDetailScreen().news to news)
                    )
                }
            )
        }
        composable(
            route = Destinations.NewsDetailScreen().route,
        ) { entry ->
            val news = entry.parcelableData<News>(Destinations.NewsDetailScreen().news)
            NewsDetailRoute(news = news)
        }
    }
}