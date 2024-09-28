package ir.composenews.appwatch.navigation.graph

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import ir.composenews.appwatch.navigation.MainContract
import ir.composenews.appwatch.ui.details.MarketDetailWearRoute
import ir.composenews.uimarket.model.MarketModel

fun NavGraphBuilder.wearMarketDetail(uiState: MainContract.State) {
    composable(
        route = Destinations.MarketDetailScreen().route,
    ) { entry ->
        val market = entry.parcelableData<MarketModel>(Destinations.MarketDetailScreen().market)
            ?: uiState.market as MarketModel
        MarketDetailWearRoute(
            market = market,
        )
    }
}

private fun <T> NavBackStackEntry.parcelableData(key: String): T? {
    return arguments?.parcelable(key) as? T
}

private inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= 33 ->
            getParcelable(key, T::class.java)

        else ->
            @Suppress("DEPRECATION")
            getParcelable(key) as? T
    }
