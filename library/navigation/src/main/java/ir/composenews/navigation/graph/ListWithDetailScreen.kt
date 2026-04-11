@file:Suppress("ktlint")

package ir.composenews.navigation.graph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ir.composenews.designsystem.component.LocalAnimatedVisibilityScope
import ir.composenews.designsystem.component.LocalSharedTransitionScope
import ir.composenews.marketdetail.MarketDetailRoute
import ir.composenews.marketlist.MarketListRoute
import ir.composenews.uimarket.model.MarketModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ListWithDetailScreen(
    modifier: Modifier = Modifier,
    navigator: ThreePaneScaffoldNavigator<MarketModel>,
    showFavorite: Boolean,
) {
    val scope = rememberCoroutineScope()

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this@SharedTransitionLayout) {
            NavigableListDetailPaneScaffold(
                modifier = modifier,
                navigator = navigator,
                listPane = {
                    AnimatedPane {
                        CompositionLocalProvider(LocalAnimatedVisibilityScope provides this@AnimatedPane) {
                            MarketListRoute(
                                onNavigateToDetailScreen = { market ->
                                    scope.launch {
                                        navigator.navigateTo(
                                            pane = ListDetailPaneScaffoldRole.Detail,
                                            contentKey = market,
                                        )
                                    }
                                },
                                showFavoriteList = showFavorite,
                            )
                        }
                    }
                },
                detailPane = {
                    AnimatedPane {
                        CompositionLocalProvider(LocalAnimatedVisibilityScope provides this@AnimatedPane) {
                            navigator.currentDestination?.contentKey?.let { marketContent ->
                                MarketDetailRoute(
                                    market = marketContent,
                                )
                            } ?: run {
                                Box(
                                    modifier = modifier,
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("Select item from left")
                                }
                            }
                        }
                    }
                },
            )
        }
    }
}
