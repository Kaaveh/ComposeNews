package ir.kaaveh.composenews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
                onNavigateToDetailScreen = {
                    navController.navigate(Destinations.NewsDetailScreen.route)
                }
            )
        }
        composable(Destinations.FavoriteNewsScreen.route) {
            FavoriteNewsScreen()
        }
        composable(
            route = Destinations.NewsDetailScreen.route + "/{news_link}",
            arguments = listOf(
                navArgument("news_link") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                }
            )
        ) {entry ->
            NewsDetailScreen(newsLink = entry.arguments?.getString("news_link") ?: "")
        }
    }
}