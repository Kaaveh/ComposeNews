@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import ir.composenews.apptv.navigation.MainContract
import ir.composenews.apptv.navigation.graph.Destinations
import ir.composenews.apptv.ui.mock.MockMarketDetailTvRoute
import ir.composenews.apptv.ui.mock.MockMarketListTvRoute
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ComposeNewsTvApp() {
    val viewModel: ir.composenews.tv.MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val backStack = remember { mutableStateListOf<Destinations>(Destinations.MarketListScreen) }
    val currentDestination = backStack.lastOrNull() ?: Destinations.MarketListScreen

    NavigationDrawer(
        drawerContent = {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                NavigationDrawerItem(
                    selected = currentDestination == Destinations.MarketListScreen,
                    onClick = {
                        if (currentDestination != Destinations.MarketListScreen) {
                            backStack.clear()
                            backStack.add(Destinations.MarketListScreen)
                        }
                    },
                    leadingContent = { Text(text = "M") },
                ) {
                    Text(text = "Markets")
                }
                NavigationDrawerItem(
                    selected = currentDestination == Destinations.FavoriteMarketScreen,
                    onClick = {
                        if (currentDestination != Destinations.FavoriteMarketScreen) {
                            backStack.clear()
                            backStack.add(Destinations.FavoriteMarketScreen)
                        }
                    },
                    leadingContent = { Text(text = "F") },
                ) {
                    Text(text = "Favorites")
                }
            }
        },
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier
                        .width(8.dp)
                        .fillMaxHeight(),
            ) {}
            NavigationContent(
                backStack = backStack,
                state = state,
                onMarketSelected = { market ->
                    viewModel.event(MainContract.Event.SetMarket(market))
                    backStack.add(Destinations.MarketDetailScreen)
                },
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp),
            )
        }
    }
}

@Composable
private fun NavigationContent(
    backStack: MutableList<Destinations>,
    state: MainContract.State,
    onMarketSelected: (ir.composenews.uimarket.model.MarketModel) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    MockMarketListTvRoute(
                        showFavoriteList = false,
                        onNavigateToDetailScreen = onMarketSelected,
                    )
                }
                entry<Destinations.FavoriteMarketScreen> {
                    MockMarketListTvRoute(
                        showFavoriteList = true,
                        onNavigateToDetailScreen = onMarketSelected,
                    )
                }
                entry<Destinations.MarketDetailScreen> {
                    state.market?.let { market ->
                        MockMarketDetailTvRoute(market = market)
                    } ?: Text(
                        text = "Select a market",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
    )
}
