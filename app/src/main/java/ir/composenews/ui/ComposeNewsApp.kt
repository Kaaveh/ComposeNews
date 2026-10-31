@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.composenews.designsystem.R
import ir.composenews.marketlist.FavoriteListContract
import ir.composenews.marketlist.FavoriteListViewModel
import ir.composenews.navigation.BottomNavItem
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.graph.ListWithDetailScreen
import ir.composenews.navigation.graph.isListPaneHidden
import ir.composenews.navigation.graph.marketPaneDirective
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

/** Window widths where the rail starts expanded, with the app name and the favorites count. */
private val ExpandedRailWidths = 1200.dp..<1440.dp

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
    // Measured beside the rail, so expanding the rail can drop the desktop's extra pane.
    var contentWidth by remember { mutableStateOf(Dp.Unspecified) }
    val density = LocalDensity.current
    val paneDirective = marketPaneDirective(contentWidth)
    val marketNavigator = rememberListDetailPaneScaffoldNavigator<String>(scaffoldDirective = paneDirective)
    val favoriteNavigator = rememberListDetailPaneScaffoldNavigator<String>(scaffoldDirective = paneDirective)
    val currentNavigator =
        if (currentDestination == Destinations.FavoriteMarketScreen) favoriteNavigator else marketNavigator

    val windowWidth = currentWindowDpSize().width
    val defaultRailExpanded = windowWidth in ExpandedRailWidths
    // Keyed on the default, so resizing into another breakpoint starts from that breakpoint's rail.
    var railExpanded by rememberSaveable(defaultRailExpanded) { mutableStateOf(defaultRailExpanded) }
    val navigationSuiteType = navigationSuiteType(currentNavigator.isListPaneHidden, windowWidth, railExpanded)
    val isRail = navigationSuiteType in RailTypes
    // The count shows on the expanded rail only, as drawn, so only that rail reads the favorites.
    val favoriteCount =
        if (navigationSuiteType == NavigationSuiteType.WideNavigationRailExpanded) rememberFavoriteCount() else 0

    NavigationSuiteScaffold(
        navigationItems = {
            items.forEach { item ->
                val selected = item.route == currentDestination
                NavigationSuiteItem(
                    selected = selected,
                    onClick = { navigateTo(item.route as Destinations) },
                    icon = {
                        Icon(
                            painter = painterResource(if (selected) item.selectedIcon else item.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(text = item.name) },
                    navigationSuiteType = navigationSuiteType,
                    badge = countBadge(if (item.route == Destinations.FavoriteMarketScreen) favoriteCount else 0),
                )
            }
        },
        navigationSuiteType = navigationSuiteType,
        containerColor = MaterialTheme.colorScheme.background,
        primaryActionContent = {
            if (isRail) RailHeader(expanded = railExpanded, onToggle = { railExpanded = !railExpanded })
        },
    ) {
        NavigationContent(
            backStack = backStack,
            marketNavigator = marketNavigator,
            favoriteNavigator = favoriteNavigator,
            navigateTo = navigateTo,
            modifier = Modifier.onSizeChanged { contentWidth = with(density) { it.width.toDp() } },
        )
    }
}

@Composable
private fun navigationSuiteType(
    listPaneHidden: Boolean,
    windowWidth: Dp,
    railExpanded: Boolean,
): NavigationSuiteType =
    when {
        // A phone showing a market's detail is full-screen, without the bar.
        listPaneHidden -> NavigationSuiteType.None

        windowWidth < 600.dp -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

        railExpanded -> NavigationSuiteType.WideNavigationRailExpanded

        else -> NavigationSuiteType.WideNavigationRailCollapsed
    }

private val RailTypes =
    setOf(NavigationSuiteType.WideNavigationRailExpanded, NavigationSuiteType.WideNavigationRailCollapsed)

/** A badge with [count], or none for 0. */
private fun countBadge(count: Int): (@Composable () -> Unit)? =
    if (count > 0) {
        { Badge { Text(text = "$count") } }
    } else {
        null
    }

/** The rail's menu button, which expands or collapses it, and the app name while expanded. */
@Composable
private fun RailHeader(
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onToggle) {
            Icon(
                painter = painterResource(if (expanded) R.drawable.ic_menu_open else R.drawable.ic_menu),
                contentDescription = if (expanded) "Collapse navigation" else "Expand navigation",
            )
        }
        if (expanded) {
            Text(
                text = "ComposeNews",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 4.dp, end = 12.dp),
            )
        }
    }
}

/** The favorites count for the expanded rail, from the same ViewModel the Favorites list uses. */
@Composable
private fun rememberFavoriteCount(): Int {
    val viewModel = koinViewModel<FavoriteListViewModel>()
    LaunchedEffect(viewModel) { viewModel.event(FavoriteListContract.Event.OnGetFavoriteList) }
    return viewModel.state
        .collectAsState()
        .value.count
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
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier =
            modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
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
