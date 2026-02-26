@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ir.composenews.navigation.BottomNavItem
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.graph.ListWithDetailScreen
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ComposeNewsApp() {
    val items = rememberNavigationItems()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Destinations.MarketListScreen.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            items.forEach { item ->
                item(
                    selected = item.route == currentRoute,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                    label = { Text(text = item.name) },
                )
            }
        },
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
    ) {
        val marketNavigator = rememberListDetailPaneScaffoldNavigator<MarketModel>()
        val favoriteNavigator = rememberListDetailPaneScaffoldNavigator<MarketModel>()
        NavigationContent(navController, marketNavigator, favoriteNavigator)
    }
}

@Composable
private fun rememberNavigationItems(): ImmutableList<BottomNavItem> =
    remember {
        persistentListOf(
            BottomNavItem(
                name = "Markets",
                route = Destinations.MarketListScreen.route,
                icon = Icons.Default.Home,
            ),
            BottomNavItem(
                name = "Favorite",
                route = Destinations.FavoriteMarketScreen.route,
                icon = Icons.Default.Favorite,
            ),
        )
    }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NavigationContent(
    navController: NavHostController,
    marketNavigator: ThreePaneScaffoldNavigator<MarketModel>,
    favoriteNavigator: ThreePaneScaffoldNavigator<MarketModel>,
) {
    val modifier =
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)

    NavHost(
        navController = navController,
        startDestination = Destinations.MarketListScreen.route,
        modifier = modifier,
    ) {
        composable(route = Destinations.MarketListScreen.route) {
            ListWithDetailScreen(
                modifier = Modifier.fillMaxSize(),
                navigator = marketNavigator,
                showFavorite = false,
            )
        }
        composable(route = Destinations.FavoriteMarketScreen.route) {
            ListWithDetailScreen(
                modifier = Modifier.fillMaxSize(),
                navigator = favoriteNavigator,
                showFavorite = true,
            )
        }
    }
}
