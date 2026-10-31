@file:Suppress("LongMethod", "ktlint")

package ir.composenews.navigation.graph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.defaultDragHandleSemantics
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import ir.composenews.core.ui.MarketModelSaver
import ir.composenews.designsystem.component.LocalAnimatedVisibilityScope
import ir.composenews.designsystem.component.LocalSharedTransitionScope
import ir.composenews.marketdetail.MarketDataCard
import ir.composenews.marketdetail.MarketDataPlaceholder
import ir.composenews.marketdetail.MarketDetailPlaceholder
import ir.composenews.marketdetail.MarketDetailRoute
import ir.composenews.marketdetail.MarketDetailViewModel
import ir.composenews.marketlist.FavoriteListRoute
import ir.composenews.marketlist.FavoritesCardRoute
import ir.composenews.marketlist.MarketListRoute
import ir.composenews.uimarket.model.MarketModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * From this content width the desktop extra pane (Market Data, favorites) joins the list and the detail:
 * a 1440 dp window less the 96 dp collapsed rail. Narrower, the detail would drop below its wide layout.
 */
private val ThreePaneContentWidth = 1344.dp

/** The collapsed rail's width, for the content width before the content is first measured. */
private val CollapsedRailWidth = 96.dp

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ListWithDetailScreen(
    modifier: Modifier = Modifier,
    navigator: ThreePaneScaffoldNavigator<String>,
    showFavorite: Boolean,
    onSwitchTab: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var selectedMarket: MarketModel? by rememberSaveable(stateSaver = MarketModelSaver) {
        mutableStateOf(null)
    }
    // One state for the detail pane and the desktop's Market Data pane.
    val detailViewModel = koinViewModel<MarketDetailViewModel>()
    val inPane = navigator.isPaneExpanded(ListDetailPaneScaffoldRole.List) &&
        navigator.isPaneExpanded(ListDetailPaneScaffoldRole.Detail)
    val extraShown = navigator.isPaneExpanded(ListDetailPaneScaffoldRole.Extra)

    // Switching tabs drops this entry's saved selection, but the navigator lives on in the app shell.
    // A navigator left on Detail would then show an empty detail pane, full-screen and without the bar.
    LaunchedEffect(Unit) {
        if (selectedMarket == null && navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail) {
            navigator.navigateBack()
        }
    }

    val openDetail = { market: MarketModel ->
        selectedMarket = market
        scope.launch {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.Detail,
                contentKey = market.id,
            )
        }
        Unit
    }

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this@SharedTransitionLayout) {
            NavigableListDetailPaneScaffold(
                modifier = modifier,
                navigator = navigator,
                listPane = {
                    // 384 rather than the default 360, so the filter group's labels fit.
                    AnimatedPane(modifier = Modifier.preferredWidth(384.dp)) {
                        CompositionLocalProvider(LocalAnimatedVisibilityScope provides this@AnimatedPane) {
                            val selectedId = selectedMarket?.id?.takeIf { inPane }
                            if (showFavorite) {
                                FavoriteListRoute(
                                    onNavigateToDetailScreen = openDetail,
                                    onBrowseMarkets = onSwitchTab,
                                    selectedMarketId = selectedId,
                                )
                            } else {
                                MarketListRoute(
                                    onNavigateToDetailScreen = openDetail,
                                    onOpenFavorites = onSwitchTab,
                                    selectedMarketId = selectedId,
                                    inPane = inPane,
                                )
                            }
                        }
                    }
                },
                detailPane = {
                    AnimatedPane {
                        CompositionLocalProvider(LocalAnimatedVisibilityScope provides this@AnimatedPane) {
                            selectedMarket?.let { marketContent ->
                                MarketDetailRoute(
                                    market = marketContent,
                                    // Beside the list there's nothing to go back to. inPane is false while a
                                    // phone's detail animates out too (Detail is Hidden by then), so it keeps
                                    // its layout and back button until it's gone.
                                    onBack = if (inPane) null else ({ scope.launch { navigator.navigateBack() } }),
                                    inPane = inPane,
                                    showMarketData = !extraShown,
                                    viewModel = detailViewModel,
                                )
                            } ?: MarketDetailPlaceholder()
                        }
                    }
                },
                extraPane = {
                    AnimatedPane(modifier = Modifier.preferredWidth(320.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            if (selectedMarket != null) {
                                MarketDataCard(viewModel = detailViewModel)
                            } else if (showFavorite) {
                                // Without it the pane would be blank: the list beside it already is the favorites.
                                MarketDataPlaceholder()
                            }
                            // On the Favorites tab the list beside it already is the favorites.
                            if (!showFavorite) {
                                FavoritesCardRoute(
                                    selectedMarketId = selectedMarket?.id,
                                    onMarketClick = openDetail,
                                    onSeeAll = onSwitchTab,
                                )
                            }
                        }
                    }
                },
                paneExpansionDragHandle = { state ->
                    val interactionSource = remember { MutableInteractionSource() }
                    VerticalDragHandle(
                        modifier = Modifier.paneExpansionDraggable(
                            state,
                            LocalMinimumInteractiveComponentSize.current,
                            interactionSource,
                            state.defaultDragHandleSemantics(),
                        ),
                        interactionSource = interactionSource,
                    )
                },
            )
        }
    }
}

/**
 * The panes there's room for: one below an 840 dp window, list and detail from there, and the extra pane as
 * well (three partitions expand all three panes) when [contentWidth], the width beside the rail, allows it.
 * An expanded rail can so drop the extra pane. [contentWidth] is unspecified until the content is measured.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun marketPaneDirective(contentWidth: Dp = Dp.Unspecified): PaneScaffoldDirective {
    val directive = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val width = if (contentWidth.isSpecified) contentWidth else currentWindowDpSize().width - CollapsedRailWidth
    return if (width >= ThreePaneContentWidth) directive.copy(maxHorizontalPartitions = 3) else directive
}

/** True when only the detail pane is on screen (a phone showing a market). */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
val ThreePaneScaffoldNavigator<*>.isListPaneHidden: Boolean
    get() = scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldNavigator<*>.isPaneExpanded(role: ThreePaneScaffoldRole): Boolean =
    scaffoldValue[role] == PaneAdaptedValue.Expanded
