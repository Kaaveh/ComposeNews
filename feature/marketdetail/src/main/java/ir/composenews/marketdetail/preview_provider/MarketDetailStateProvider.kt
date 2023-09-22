@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.marketdetail.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.composenews.domain.model_provider.marketList
import ir.composenews.marketdetail.MarketDetailContract

class MarketDetailStateProvider : PreviewParameterProvider<MarketDetailContract.State> {
    override val values: Sequence<MarketDetailContract.State> = sequenceOf(
        MarketDetailContract.State(
            marketList[0],
        ),
    )
}
