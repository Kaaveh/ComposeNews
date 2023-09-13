package ir.composenews.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import ir.composenews.base.MainContract
import ir.composenews.domain.model.Market
import ir.composenews.navigation.BottomNavItem
import ir.composenews.navigation.ComposeNewsNavHost
import ir.composenews.navigation.Destinations
import ir.composenews.ui.component.BottomNavigationBar
import ir.composenews.ui.component.ComposeNewsNavigationRail
import ir.composenews.utils.ContentType
import ir.composenews.utils.DevicePosture
import ir.composenews.utils.NavigationType
import ir.composenews.utils.isBookPosture
import ir.composenews.utils.isSeparating

@Composable
fun ComposeNewsApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    onMarketSelected: ((Market, ContentType) -> Unit)? = null,
    closeDetailScreen: () -> Unit,
    uiState: MainContract.State
) {

    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: NavigationType
    val contentType: ContentType

    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                ContentType.DUAL_PANE
            } else {
                ContentType.SINGLE_PANE
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = ContentType.DUAL_PANE
        }

        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.SINGLE_PANE
        }
    }

    ComposeNewsAppWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        onMarketSelected = onMarketSelected,
        closeDetailScreen = closeDetailScreen,
        uiState = uiState
    )

}

@Composable
fun ComposeNewsAppWrapper(
    navigationType: NavigationType,
    contentType: ContentType,
    displayFeatures: List<DisplayFeature>,
    onMarketSelected: ((Market, ContentType) -> Unit)? = null,
    closeDetailScreen: () -> Unit,
    uiState: MainContract.State
) {
    val items = remember {
        listOf(
            BottomNavItem(
                name = "Markets",
                route = Destinations.MarketListScreen.route,
                icon = Icons.Default.Home
            ),
            BottomNavItem(
                name = "Favorite",
                route = Destinations.FavoriteMarketScreen.route,
                icon = Icons.Default.Favorite,
            ),
        )
    }

    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentScreenRoute = backStackEntry.value?.destination?.route
    val bottomNavVisible =
        navigationType == NavigationType.BOTTOM_NAVIGATION && !uiState.isDetailOnlyOpen


    Scaffold { paddingValues ->
        Row(
            modifier = Modifier.padding(paddingValues)
        ) {
            AnimatedVisibility(visible = navigationType == NavigationType.NAVIGATION_RAIL) {
                ComposeNewsNavigationRail(
                    items = items,
                    currentScreenRoute = currentScreenRoute,
                    onItemClick = { navController.navigate(it.route) })
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ComposeNewsNavHost(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = paddingValues.calculateBottomPadding()),
                    navController = navController,
                    contentType = contentType,
                    displayFeatures = displayFeatures,
                    onMarketSelected = onMarketSelected,
                    closeDetailScreen = closeDetailScreen,
                    uiState = uiState
                )
                AnimatedVisibility(visible = bottomNavVisible) {
                    BottomNavigationBar(
                        items = items,
                        currentScreenRoute = currentScreenRoute,
                        onItemClick = { navController.navigate(it.route) }
                    )
                }
            }

        }
    }
}