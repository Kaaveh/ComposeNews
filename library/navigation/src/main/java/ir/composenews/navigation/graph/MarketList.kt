package ir.composenews.navigation.graph

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import ir.composenews.marketdetail.MarketDetailRoute
import ir.composenews.marketlist.MarketListRoute
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.MainContract
import ir.composenews.navigation.extension_function.parcelableData
import ir.composenews.uimarket.model.MarketModel
import ir.composenews.utils.ContentType
import kotlinx.collections.immutable.PersistentList

fun NavGraphBuilder.marketList(
    showFavorite: Boolean,
    contentType: ContentType,
    displayFeature: PersistentList<DisplayFeature>,
    uiState: MainContract.State,
    closeDetailScreen: () -> Unit,
    onMarketSelected: ((MarketModel, ContentType) -> Unit)? = null,
) {
    composable(Destinations.MarketListScreen.route) { entry ->
        when (contentType) {
            ContentType.SINGLE_PANE -> SingleListScreen(
                showFavorite = showFavorite,
                uiState = uiState,
                onMarketSelected = onMarketSelected,
                contentType = contentType,
                closeDetailScreen = closeDetailScreen,
            )

            ContentType.DUAL_PANE -> {
                val market =
                    entry.parcelableData<MarketModel>(Destinations.MarketDetailScreen().market)
                        ?: uiState.market
                ListWithDetailScreen(
                    displayFeatures = displayFeature,
                    market = market,
                    showFavorite = showFavorite,
                    uiState = uiState,
                    onMarketSelected = onMarketSelected,
                    closeDetailScreen = closeDetailScreen,
                    contentType = contentType,
                )
            }
        }
    }
}

@Composable
fun SingleListScreen(
    showFavorite: Boolean,
    uiState: MainContract.State,
    closeDetailScreen: () -> Unit,
    contentType: ContentType,
    onMarketSelected: ((MarketModel, ContentType) -> Unit)? = null,
) {
    if (uiState.market != null && uiState.isDetailOnlyOpen) {
        BackHandler {
            closeDetailScreen()
        }
        MarketDetailRoute(
            market = uiState.market,
        )
    } else {
        MarketListRoute(
            onNavigateToDetailScreen = { market ->
                onMarketSelected?.invoke(market, contentType)
            },
            isDetailOnlyOpen = uiState.isDetailOnlyOpen,
            marketModel = uiState.market,
            showFavoriteList = showFavorite,
            closeDetailScreen = closeDetailScreen,
            contentType = contentType,
        )
    }
}

@Composable
fun ListWithDetailScreen(
    showFavorite: Boolean,
    displayFeatures: PersistentList<DisplayFeature>,
    market: MarketModel?,
    uiState: MainContract.State,
    contentType: ContentType,
    closeDetailScreen: () -> Unit,
    onMarketSelected: ((MarketModel, ContentType) -> Unit)? = null,
) {
    TwoPane(
        first = {
            MarketListRoute(
                onNavigateToDetailScreen = { market ->
                    onMarketSelected?.invoke(market, contentType)
                },
                isDetailOnlyOpen = uiState.isDetailOnlyOpen,
                marketModel = uiState.market,
                showFavoriteList = showFavorite,
                closeDetailScreen = closeDetailScreen,
                contentType = contentType,
            )
        },
        second = {
            MarketDetailRoute(
                market = market,
            )
        },
        strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
        displayFeatures = displayFeatures,
    )
}
