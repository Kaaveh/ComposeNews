package ir.composenews.apptv.navigation.graph

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destinations : NavKey {
    @Serializable
    data object MarketListScreen : Destinations

    @Serializable
    data object FavoriteMarketScreen : Destinations

    @Serializable
    data object MarketDetailScreen : Destinations
}
