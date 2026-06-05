@file:Suppress("ktlint")

package ir.composenews.apptv.navigation.graph

import androidx.compose.runtime.Composable
import ir.composenews.apptv.ui.details.MarketDetailTvRoute
import ir.composenews.uimarket.model.MarketModel

@Composable
fun TvMarketDetailEntry(market: MarketModel) {
    MarketDetailTvRoute(market = market)
}
