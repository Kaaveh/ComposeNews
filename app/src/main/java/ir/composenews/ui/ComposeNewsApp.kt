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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.composenews.navigation.BottomNavItem
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.graph.ListWithDetailScreen
import ir.composenews.uimarket.model.MarketModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ComposeNewsApp() {
    val items = rememberNavigationItems()
    val backStack = remember { androidx.compose.runtime.mutableStateListOf<Destinations>(Destinations.MarketListScreen) }
    val currentDestination = backStack.lastOrNull() ?: Destinations.MarketListScreen

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            items.forEach { item ->
                item(
                    selected = item.route == currentDestination,
                    onClick = {
                        if (item.route != currentDestination) {
                            backStack.clear()
                            backStack.add(item.route as Destinations)
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
        NavigationContent(backStack, marketNavigator, favoriteNavigator)
    }
}

@Composable
private fun rememberNavigationItems(): ImmutableList<BottomNavItem> =
    remember {
        persistentListOf(
            BottomNavItem(
                name = "Markets",
                route = Destinations.MarketListScreen,
                icon = Icons.Default.Home,
            ),
            BottomNavItem(
                name = "Favorite",
                route = Destinations.FavoriteMarketScreen,
                icon = Icons.Default.Favorite,
            ),
        )
    }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NavigationContent(
    backStack: MutableList<Destinations>,
    marketNavigator: ThreePaneScaffoldNavigator<MarketModel>,
    favoriteNavigator: ThreePaneScaffoldNavigator<MarketModel>,
) {
    val modifier =
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
            ),
        entryProvider =
            entryProvider {
                entry<Destinations.MarketListScreen> {
                    ListWithDetailScreen(
                        modifier = Modifier.fillMaxSize(),
                        navigator = marketNavigator,
                        showFavorite = false,
                    )
                }
                entry<Destinations.FavoriteMarketScreen> {
                    ListWithDetailScreen(
                        modifier = Modifier.fillMaxSize(),
                        navigator = favoriteNavigator,
                        showFavorite = true,
                    )
                }
            },
    )
}
