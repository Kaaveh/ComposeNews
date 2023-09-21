@file:Suppress("MagicNumber")

package ir.composenews.navigation.graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.composenews.base.MainContract
import ir.composenews.domain.model.Market
import ir.composenews.marketdetail.MarketDetailRoute
import ir.composenews.navigation.Destinations
import ir.composenews.navigation.extension_function.parcelableData

fun NavGraphBuilder.marketDetail(
    uiState: MainContract.State,
) {
    composable(
        route = Destinations.MarketDetailScreen().route,
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
            )
        },
        exitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End,
            )
        },
    ) { entry ->
        val market = entry.parcelableData<Market>(Destinations.MarketDetailScreen().market)
            ?: uiState.market as Market
        MarketDetailRoute(
            market = market,
        )
    }
}
