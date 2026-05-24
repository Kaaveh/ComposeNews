@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ir.composenews.apptv.navigation.MainContract
import ir.composenews.apptv.navigation.graph.Destinations
import ir.composenews.apptv.ui.mock.MockMarketDetailTvRoute
import ir.composenews.apptv.ui.mock.MockMarketListTvRoute
import org.koin.androidx.compose.koinViewModel

private val RAIL_WIDTH = 220.dp
private val RAIL_ITEM_SHAPE = RoundedCornerShape(12.dp)

@Composable
fun ComposeNewsTvApp() {
    val viewModel: ir.composenews.tv.MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val backStack = remember { mutableStateListOf<Destinations>(Destinations.MarketListScreen) }
    val currentDestination = backStack.lastOrNull() ?: Destinations.MarketListScreen

    Row(modifier = Modifier.fillMaxSize()) {
        Rail(
            currentDestination = currentDestination,
            onTabSelected = { destination ->
                if (currentDestination != destination) {
                    backStack.clear()
                    backStack.add(destination)
                }
            },
            modifier =
                Modifier
                    .width(RAIL_WIDTH)
                    .fillMaxHeight(),
        )
        NavigationContent(
            backStack = backStack,
            state = state,
            onMarketSelected = { market ->
                viewModel.event(MainContract.Event.SetMarket(market))
                backStack.add(Destinations.MarketDetailScreen)
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun Rail(
    currentDestination: Destinations,
    onTabSelected: (Destinations) -> Unit,
    modifier: Modifier = Modifier,
) {
    val firstItemFocusRequester = remember { FocusRequester() }
    val lastItemFocusRequester = remember { FocusRequester() }

    Column(
        modifier =
            modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .focusRestorer(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RailItem(
            icon = Icons.Default.Home,
            label = "Markets",
            selected = currentDestination == Destinations.MarketListScreen,
            onClick = { onTabSelected(Destinations.MarketListScreen) },
            modifier =
                Modifier
                    .focusRequester(firstItemFocusRequester)
                    .focusProperties { up = firstItemFocusRequester },
        )
        RailItem(
            icon = Icons.Default.Favorite,
            label = "Favorites",
            selected = currentDestination == Destinations.FavoriteMarketScreen,
            onClick = { onTabSelected(Destinations.FavoriteMarketScreen) },
            modifier =
                Modifier
                    .focusRequester(lastItemFocusRequester)
                    .focusProperties { down = lastItemFocusRequester },
        )
    }
}

@Composable
private fun RailItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val containerColor =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        }
    val contentColor =
        if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    val borderModifier =
        if (isFocused) {
            Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RAIL_ITEM_SHAPE,
            )
        } else {
            Modifier
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RAIL_ITEM_SHAPE)
                .background(containerColor)
                .then(borderModifier)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
        )
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.titleMedium,
        )
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
        modifier = modifier.focusRestorer(),
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
