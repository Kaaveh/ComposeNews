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
import androidx.compose.ui.unit.dp
import ir.composenews.base.LoadableComponent
import ir.composenews.base.LoadableData
import ir.composenews.designsystem.component.shimmerEffect
import ir.composenews.domain.model.MarketChart
import ir.composenews.designsystem.component.QuadLineChart as DesignSystemQuadLineChart

@Composable
internal fun QuadLineChart(
    loadableData: LoadableData<MarketChart>,
) {
    LoadableComponent(
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(20.dp)
                    .clip(MaterialTheme.shapes.large)
                    .shimmerEffect(),
            )
        },
        loaded = { data ->
            DesignSystemQuadLineChart(
                data = data.prices,
            )
        },
        error = { error -> },
        loadableData = loadableData,
    )
}
