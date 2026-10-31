@file:Suppress("MagicNumber", "MaxLineLength", "ktlint")

package ir.composenews.marketdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.composenews.base.LoadableData
import ir.composenews.base.errorViewMapper
import ir.composenews.core.ui.LoadableComponent
import ir.composenews.designsystem.component.ConnectedButtonGroup
import ir.composenews.designsystem.component.PriceChart
import ir.composenews.designsystem.component.SkeletonBlock
import ir.composenews.domain.model.MarketChart
import ir.composenews.marketdetail.MarketDetailContract.ChartRange

internal const val MARKET_DETAIL_CHART_TEST_TAG = "marketDetailChart"

private val ChartHeight = 188.dp

/** The chart card: the price line for [range], and the 1D / 1W / 1M / 1Y group under it. In a pane it's white on the pane's card. */
@Composable
internal fun MarketChart(
    loadableData: LoadableData<MarketChart>,
    range: ChartRange,
    onRangeSelect: (ChartRange) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    rangeColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(containerColor)
            .padding(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val placeholder: @Composable () -> Unit = {
            SkeletonBlock(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(ChartHeight))
        }
        LoadableComponent(
            loadableData = loadableData,
            loading = placeholder,
            initial = placeholder,
            error = { error ->
                Box(modifier = Modifier.fillMaxWidth().height(ChartHeight), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorViewMapper(error),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            },
            loaded = { data ->
                PriceChart(
                    prices = data.prices,
                    height = ChartHeight,
                    modifier = Modifier.testTag(MARKET_DETAIL_CHART_TEST_TAG),
                )
            },
        )
        ConnectedButtonGroup(
            options = ChartRange.entries,
            selected = range,
            onSelect = onRangeSelect,
            label = { it.shortLabel },
            height = 40.dp,
            uncheckedContainerColor = rangeColor,
        )
    }
}

internal val ChartRange.shortLabel: String
    get() = when (this) {
        ChartRange.Day -> "1D"
        ChartRange.Week -> "1W"
        ChartRange.Month -> "1M"
        ChartRange.Year -> "1Y"
    }

/** Next to the change chip: the period the change covers. */
internal val ChartRange.label: String
    get() = when (this) {
        ChartRange.Day -> "Today"
        ChartRange.Week -> "Past week"
        ChartRange.Month -> "Past month"
        ChartRange.Year -> "Past year"
    }
