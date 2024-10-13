package ir.composenews.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ComposeNewsApp() {
    val items = remember {
        persistentListOf(
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

    var currentRoute by remember { mutableStateOf(Destinations.MarketListScreen.route) }

    Scaffold { paddingValues ->
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                items.forEach { item ->
                    item(selected = item.route == currentRoute, onClick = {
                        currentRoute = item.route
                    }, icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                        )
                    }, label = {
                        Text(
                            text = item.name,
                        )
                    })
                }
            },
            layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                currentWindowAdaptiveInfo()
            )
        ) {
            val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
            when (currentRoute) {
                Destinations.MarketListScreen.route -> {
                    ListWithDetailScreen(
                        Modifier.padding(paddingValues),
                        navigator,
                        showFavorite = false,
                    )
                }

                Destinations.FavoriteMarketScreen.route -> {
                    ListWithDetailScreen(
                        Modifier.padding(paddingValues),
                        navigator,
                        showFavorite = true,
                    )
                }
            }
        }
    }
}