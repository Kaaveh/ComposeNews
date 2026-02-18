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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ir.composenews.navigation.BottomNavItem
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.graph.ListWithDetailScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ComposeNewsApp() {
    val items = rememberNavigationItems()
    var currentRoute by remember { mutableStateOf(Destinations.MarketListScreen.route) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            items.forEach { item ->
                item(
                    selected = item.route == currentRoute,
                    onClick = { currentRoute = item.route },
                    icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                    label = { Text(text = item.name) },
                )
            }
        },
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
    ) {
        val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
        NavigationContent(currentRoute, navigator)
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
    currentRoute: String,
    navigator: ThreePaneScaffoldNavigator<Any>,
) {
    val modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeDrawing)
    
    when (currentRoute) {
        Destinations.MarketListScreen.route -> {
            ListWithDetailScreen(modifier, navigator, showFavorite = false)
        }

        Destinations.FavoriteMarketScreen.route -> {
            ListWithDetailScreen(modifier, navigator, showFavorite = true)
        }
    }
}
