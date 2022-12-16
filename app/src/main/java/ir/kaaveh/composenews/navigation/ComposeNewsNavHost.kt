package ir.kaaveh.composenews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.kaaveh.domain.model.News
import ir.kaaveh.favoritenews.FavoriteNewsScreen
import ir.kaaveh.newsdetail.NewsDetailScreen
import ir.kaaveh.newslist.NewsListScreen

@Composable
fun ComposeNewsNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Destinations.NewsListScreen.route,
        modifier = modifier
    ) {
        composable(Destinations.NewsListScreen.route) {
            NewsListScreen(
                onNavigateToDetailScreen = { news ->
                    navController.navigate(
                        route = Destinations.NewsDetailScreen().route,
                        args = bundleOf(Destinations.NewsDetailScreen().news to news)
                    )
                }
            )
        }
        composable(Destinations.FavoriteNewsScreen.route) {
            FavoriteNewsScreen(
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
            NewsDetailScreen(news = news)
        }
    }
}