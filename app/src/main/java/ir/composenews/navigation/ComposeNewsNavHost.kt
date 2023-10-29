package ir.composenews.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.window.layout.DisplayFeature
import ir.composenews.navigation.graph.favoriteList
import ir.composenews.navigation.graph.marketDetail
import ir.composenews.navigation.graph.marketList
import ir.composenews.uimarket.model.MarketModel
import ir.composenews.utils.ContentType
import kotlinx.collections.immutable.PersistentList

@Composable
fun ComposeNewsNavHost(
    navController: NavHostController,
    modifier: Modifier,
    contentType: ContentType,
    displayFeatures: PersistentList<DisplayFeature>,
    onMarketSelected: ((MarketModel, ContentType) -> Unit)? = null,
    closeDetailScreen: () -> Unit,
    uiState: MainContract.State
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.MarketListScreen.route,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        marketList(
            displayFeature = displayFeatures,
            contentType = contentType,
            showFavorite = false,
            onMarketSelected = onMarketSelected,
            closeDetailScreen = closeDetailScreen,
            uiState = uiState
        )
        favoriteList(
            displayFeature = displayFeatures,
            contentType = contentType,
            onMarketSelected = onMarketSelected,
            closeDetailScreen = closeDetailScreen,
            uiState = uiState
        )
        marketDetail(
            uiState = uiState,
        )
    }
}