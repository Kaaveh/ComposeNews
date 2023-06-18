package ir.kaaveh.marketdetail.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.marketList
import ir.kaaveh.marketdetail.MarketDetailContract

class MarketDetailStateProvider : PreviewParameterProvider<MarketDetailContract.State> {
    override val values: Sequence<MarketDetailContract.State> = sequenceOf(
        MarketDetailContract.State(
            marketList[0],
        ),
    )
}