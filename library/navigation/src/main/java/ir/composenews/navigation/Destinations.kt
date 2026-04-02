@file:Suppress("ktlint")

package ir.composenews.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Destinations : NavKey {
    @Serializable
    data object MarketListScreen : Destinations

    @Serializable
    data object FavoriteMarketScreen : Destinations
}
