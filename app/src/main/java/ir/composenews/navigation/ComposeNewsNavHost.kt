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
import androidx.window.layout.DisplayFeature
import ir.kaaveh.base.MainContract
import ir.kaaveh.designsystem.utils.ContentType
import ir.kaaveh.domain.model.Market
import ir.kaaveh.navigation.Destinations
import ir.kaaveh.navigation.graph.favoriteList
import ir.kaaveh.navigation.graph.marketDetail
import ir.kaaveh.navigation.graph.marketList

@Composable
fun ComposeNewsNavHost(
    navController: NavHostController,
    modifier: Modifier,
    contentType: ContentType,
    displayFeatures: List<DisplayFeature>,
    onMarketSelected: ((Market, ContentType) -> Unit)? = null,
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