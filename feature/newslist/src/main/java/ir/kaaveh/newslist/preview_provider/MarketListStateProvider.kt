package ir.kaaveh.newslist.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.kaaveh.domain.model_provider.marketList
import ir.kaaveh.newslist.MarketListContract

class MarketListStateProvider : PreviewParameterProvider<MarketListContract.State> {
    override val values: Sequence<MarketListContract.State> = sequenceOf(
        MarketListContract.State(
            marketList = marketList,
        ),
    )
}