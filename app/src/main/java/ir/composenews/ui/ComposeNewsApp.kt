@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.composenews.designsystem.R
import ir.composenews.navigation.BottomNavItem
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.graph.ListWithDetailScreen
import ir.composenews.navigation.graph.isListPaneHidden
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ComposeNewsApp() {
    val items = rememberNavigationItems()
    val backStack = remember { mutableStateListOf<Destinations>(Destinations.MarketListScreen) }
    val currentDestination = backStack.lastOrNull() ?: Destinations.MarketListScreen
    val navigateTo = { destination: Destinations ->
        if (destination != currentDestination) {
            backStack.clear()
            backStack.add(destination)
        }
    }
    val marketNavigator = rememberListDetailPaneScaffoldNavigator<String>()
    val favoriteNavigator = rememberListDetailPaneScaffoldNavigator<String>()
    val currentNavigator =
        if (currentDestination == Destinations.FavoriteMarketScreen) favoriteNavigator else marketNavigator

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            items.forEach { item ->
                val selected = item.route == currentDestination
                item(
                    selected = selected,
                    onClick = { navigateTo(item.route as Destinations) },
                    icon = {
                        Icon(
                            painter = painterResource(if (selected) item.selectedIcon else item.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(text = item.name) },
                )
            }
        },
        // A phone showing a market's detail is full-screen, without the bar.
        layoutType =
            if (currentNavigator.isListPaneHidden) {
                NavigationSuiteType.None
            } else {
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
            },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        NavigationContent(backStack, marketNavigator, favoriteNavigator, navigateTo)
    }
}

@Composable
private fun rememberNavigationItems(): ImmutableList<BottomNavItem> =
    remember {
        persistentListOf(
            BottomNavItem(
                name = "Markets",
                route = Destinations.MarketListScreen,
                icon = R.drawable.ic_finance_mode,
                selectedIcon = R.drawable.ic_finance_mode_filled,
            ),
            BottomNavItem(
                name = "Favorites",
                route = Destinations.FavoriteMarketScreen,
                icon = R.drawable.ic_favorite,
                selectedIcon = R.drawable.ic_favorite_filled,
            ),
        )
    }

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun NavigationContent(
    backStack: MutableList<Destinations>,
    marketNavigator: ThreePaneScaffoldNavigator<String>,
    favoriteNavigator: ThreePaneScaffoldNavigator<String>,
    navigateTo: (Destinations) -> Unit,
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
                        onSwitchTab = { navigateTo(Destinations.FavoriteMarketScreen) },
                    )
                }
                entry<Destinations.FavoriteMarketScreen> {
                    ListWithDetailScreen(
                        modifier = Modifier.fillMaxSize(),
                        navigator = favoriteNavigator,
                        showFavorite = true,
                        onSwitchTab = { navigateTo(Destinations.MarketListScreen) },
                    )
                }
            },
    )
}
