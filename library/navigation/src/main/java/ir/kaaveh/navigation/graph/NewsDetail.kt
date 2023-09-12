package ir.kaaveh.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kaaveh.base.MainContract
import ir.kaaveh.domain.model.Market
import ir.kaaveh.marketdetail.MarketDetailRoute
import ir.kaaveh.navigation.Destinations
import ir.kaaveh.navigation.extension_function.parcelableData

fun NavGraphBuilder.marketDetail(
    uiState: MainContract.State,
) {
    composable(
        route = Destinations.MarketDetailScreen().route,
    ) { entry ->
        val market = entry.parcelableData<Market>(Destinations.MarketDetailScreen().market)
            ?: uiState.market as Market
        MarketDetailRoute(
            market = market,
        )
    }
}