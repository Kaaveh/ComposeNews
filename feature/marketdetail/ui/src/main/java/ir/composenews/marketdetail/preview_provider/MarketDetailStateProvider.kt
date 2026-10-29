@file:Suppress("PackageNaming", "PackageName", "MagicNumber", "ktlint")

package ir.composenews.marketdetail.preview_provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ir.composenews.base.LoadableData
import ir.composenews.domain.model.MarketChart
import ir.composenews.domain.model.MarketDetail
import ir.composenews.core.ui.marketList
import ir.composenews.marketdetail.MarketDetailContract
import ir.composenews.network.Errors
import ir.composenews.uimarket.mapper.toMarketModel
import kotlinx.collections.immutable.persistentListOf

class MarketDetailStateProvider : PreviewParameterProvider<MarketDetailContract.State> {
    override val values: Sequence<MarketDetailContract.State> = sequenceOf(
        MarketDetailContract.State(
            market = LoadableData.Loading,
            marketChart = LoadableData.Loading,
            marketDetail = LoadableData.Loading,
        ),
        MarketDetailContract.State(
            market = LoadableData.Loaded(data = marketList[0].toMarketModel()),
            marketChart = LoadableData.Loaded(
                data = MarketChart(
                    prices = persistentListOf(
                        -1000L to 45000.0,
                        -500L to 46000.5,
                    ),
                ),
            ),
            marketDetail = LoadableData.Loaded(
                data = MarketDetail(
                    id = "1",
                    marketCapRank = 2,
                    name = "name",
                    marketData = null,
                ),
            ),
        ),
        MarketDetailContract.State(
            market = LoadableData.Error(
                error = Errors.ExceptionError(
                    message = "some exception",
                    throwable = Throwable("some exception"),
                ),
            ),
            marketChart = LoadableData.Error(
                error = Errors.ExceptionError(
                    message = "some exception",
                    throwable = Throwable("some exception"),
                ),
            ),
            marketDetail = LoadableData.Error(
                error = Errors.ExceptionError(
                    message = "some exception",
                    throwable = Throwable("some exception"),
                ),
            ),
        ),
    )
}
