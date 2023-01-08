package ir.kaaveh.composenews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ir.kaaveh.navigation.graph.favoriteNews
import ir.kaaveh.navigation.graph.newsDetail
import ir.kaaveh.navigation.graph.newsList
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.navigation.Destinations

@Composable
fun ComposeNewsNavHost(
    navController: NavHostController,
    modifier: Modifier,
    onProvideBaseViewModel: (baseViewModel: BaseViewModel) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.NewsListScreen.route,
        modifier = modifier,
    ) {
        newsList(navController, onProvideBaseViewModel)
        favoriteNews(navController, onProvideBaseViewModel)
        newsDetail(onProvideBaseViewModel)
    }
}