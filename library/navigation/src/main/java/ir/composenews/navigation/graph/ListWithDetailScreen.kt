package ir.composenews.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ir.composenews.marketdetail.MarketDetailRoute
import ir.composenews.marketlist.MarketListRoute
import ir.composenews.uimarket.model.MarketModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListWithDetailScreen(
    modifier: Modifier = Modifier,
    navigator: ThreePaneScaffoldNavigator<Any>,
    showFavorite: Boolean,
) {
    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            MarketListRoute(
                onNavigateToDetailScreen = { market ->
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        content = market,
                    )
                },
                showFavoriteList = showFavorite,
            )
        },
        detailPane = {
            (navigator.currentDestination?.content as? MarketModel)?.let { content ->
                MarketDetailRoute(
                    market = content,
                )
            } ?: run {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Select item from left")
                }
            }
        },
    )
}
