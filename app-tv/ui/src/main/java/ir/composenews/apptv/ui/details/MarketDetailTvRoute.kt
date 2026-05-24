@file:Suppress("ktlint")

package ir.composenews.apptv.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.composenews.marketdetail.MarketDetailRoute
import ir.composenews.uimarket.model.MarketModel

@Composable
fun MarketDetailTvRoute(market: MarketModel) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp, vertical = 24.dp),
    ) {
        MarketDetailRoute(market = market)
    }
}
