package ir.composenews.marketlist.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.marketList
import ir.composenews.marketlist.MarketListContract

class MarketListStateProvider : PreviewParameterProvider<MarketListContract.State> {
    override val values: Sequence<MarketListContract.State> = sequenceOf(
        MarketListContract.State(
            marketList = marketList,
        ),
    )
}