@file:Suppress("PackageNaming", "PackageName", "ktlint")

package ir.composenews.marketlist.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.composenews.base.LoadableData
import ir.composenews.domain.model_provider.marketList
import ir.composenews.marketlist.MarketListContract
import ir.composenews.uimarket.mapper.toMarketModel
import kotlinx.collections.immutable.toPersistentList

class MarketListStateProvider : PreviewParameterProvider<MarketListContract.State> {
    override val values: Sequence<MarketListContract.State> = sequenceOf(
        MarketListContract.State(
            favoriteMarketList = LoadableData.Loaded(
                data = marketList.map { it.toMarketModel() }
                    .toPersistentList(),
            ),
        ),
    )
}
