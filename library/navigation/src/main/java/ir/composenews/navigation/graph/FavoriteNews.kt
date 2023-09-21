@file:Suppress("ImportOrdering")

package ir.composenews.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import ir.composenews.base.MainContract
import ir.composenews.domain.model.Market
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.extension_function.parcelableData
import ir.composenews.utils.ContentType

fun NavGraphBuilder.favoriteList(
    contentType: ContentType,
    displayFeature: List<DisplayFeature>,
    uiState: MainContract.State,
    closeDetailScreen: () -> Unit,
    onMarketSelected: ((Market, ContentType) -> Unit)? = null,
) {
    composable(Destinations.FavoriteMarketScreen.route) { entry ->
        when (contentType) {
            ContentType.SINGLE_PANE -> SingleListScreen(
                showFavorite = true,
                uiState = uiState,
                onMarketSelected = onMarketSelected,
                closeDetailScreen = closeDetailScreen,
                contentType = contentType,
            )

            ContentType.DUAL_PANE -> {
                val market = entry.parcelableData<Market>(Destinations.MarketDetailScreen().market)
                    ?: (uiState.market as? Market?)
                ListWithDetailScreen(
                    displayFeatures = displayFeature,
                    market = market,
                    showFavorite = true,
                    uiState = uiState,
                    onMarketSelected = onMarketSelected,
                    closeDetailScreen = closeDetailScreen,
                    contentType = contentType,
                )
            }
        }
    }
}
