

package ir.composenews.marketlist.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.composenews.domain.model_provider.marketList
import ir.composenews.marketlist.MarketListContract
import ir.composenews.uimarket.mapper.toMarketModel
import kotlinx.collections.immutable.toPersistentList

class MarketListStateProvider : PreviewParameterProvider<MarketListContract.State> {
    override val values: Sequence<MarketListContract.State> = sequenceOf(
        MarketListContract.State(
            marketList = marketList.map { it.toMarketModel() }.toPersistentList(),
        ),
    )
}
