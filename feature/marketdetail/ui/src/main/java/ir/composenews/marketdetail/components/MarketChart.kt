@file:Suppress("ktlint")

package ir.composenews.marketdetail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ir.composenews.core.ui.LoadableComponent
import ir.composenews.base.LoadableData
import ir.composenews.designsystem.component.PriceChart
import ir.composenews.designsystem.component.shimmerEffect
import ir.composenews.domain.model.MarketChart

internal const val MARKET_DETAIL_CHART_TEST_TAG = "marketDetailChart"

@Composable
internal fun MarketChart(
    loadableData: LoadableData<MarketChart>,
) {
    LoadableComponent(
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(188.dp)
                    .clip(MaterialTheme.shapes.large)
                    .shimmerEffect(),
            )
        },
        loaded = { data ->
            PriceChart(
                prices = data.prices,
                modifier = Modifier
                    .padding(20.dp)
                    .testTag(MARKET_DETAIL_CHART_TEST_TAG),
            )
        },
        error = { error -> },
        loadableData = loadableData,
    )
}
