@file:Suppress("MagicNumber", "MaxLineLength", "ktlint")

package ir.composenews.marketdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.composenews.base.LoadableData
import ir.composenews.base.errorViewMapper
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.StatTile
import ir.composenews.designsystem.component.formatCompactUsd
import ir.composenews.designsystem.component.formatPrice
import ir.composenews.designsystem.component.SkeletonBlock
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.designsystem.theme.groupedRowShape
import ir.composenews.designsystem.theme.tabularNums
import ir.composenews.domain.model.MarketDetail

/** Tiles in reading order; the corner each one turns towards the middle of the grid is 8 dp. */
private val TileShapes = listOf(
    RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomEnd = 8.dp, bottomStart = 28.dp),
    RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomEnd = 28.dp, bottomStart = 8.dp),
    RoundedCornerShape(topStart = 28.dp, topEnd = 8.dp, bottomEnd = 28.dp, bottomStart = 28.dp),
    RoundedCornerShape(topStart = 8.dp, topEnd = 28.dp, bottomEnd = 28.dp, bottomStart = 28.dp),
)

/** One row of four on a wide pane: 24 dp at the row's ends, 8 dp between tiles. */
private val RowTileShapes = listOf(
    RoundedCornerShape(topStart = 24.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 24.dp),
    RoundedCornerShape(8.dp),
    RoundedCornerShape(8.dp),
    RoundedCornerShape(topStart = 8.dp, topEnd = 24.dp, bottomEnd = 24.dp, bottomStart = 8.dp),
)

private const val NO_VALUE = "—"

private const val TILE_COUNT = 4

/** Grid: 2×2 tiles (phone, foldable pane). Row: four tiles (wide pane). List: label and value rows (desktop extra pane). */
internal enum class MarketDataLayout { Grid, Row, List }

/** `Market Data`: Market Cap, Rank, High 24h and Low 24h. */
@Composable
internal fun MarketData(
    loadableData: LoadableData<MarketDetail>,
    layout: MarketDataLayout = MarketDataLayout.Grid,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Market Data",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .semantics { heading() },
        )
        when (loadableData) {
            is LoadableData.Error -> Text(
                text = errorViewMapper(loadableData.error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )

            is LoadableData.Loaded -> if (layout == MarketDataLayout.List) {
                MarketDataList(detail = loadableData.data)
            } else {
                MarketDataTiles(detail = loadableData.data, layout = layout)
            }

            LoadableData.Loading, LoadableData.Initial -> when (layout) {
                MarketDataLayout.List -> Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    repeat(TILE_COUNT) {
                        SkeletonBlock(shape = groupedRowShape(it, TILE_COUNT), modifier = Modifier.fillMaxWidth().height(52.dp))
                    }
                }
                else -> TileGrid(layout) { index, shape, modifier ->
                    SkeletonBlock(shape = shape, modifier = modifier.height(108.dp))
                }
            }
        }
    }
}

/** The four values in tile order. */
private fun MarketDetail.values(): List<Pair<String, String>> {
    val data = marketData
    return listOf(
        "Market Cap" to (data?.let { formatCompactUsd(it.marketCapUSD) } ?: NO_VALUE),
        "Rank" to "#${data?.marketCapRank ?: marketCapRank}",
        "High 24h" to (data?.let { formatPrice(it.high24hUSD) } ?: NO_VALUE),
        "Low 24h" to (data?.let { formatPrice(it.low24hUSD) } ?: NO_VALUE),
    )
}

@Composable
private fun MarketDataList(detail: MarketDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        detail.values().forEachIndexed { index, (label, value) ->
            val highlighted = index == 0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(groupedRowShape(index, TILE_COUNT))
                    .background(
                        if (highlighted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest,
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .semantics(mergeDescendants = true) {},
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highlighted) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight(650)).tabularNums(),
                    color = if (highlighted) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun MarketDataTiles(detail: MarketDetail, layout: MarketDataLayout) {
    val values = detail.values()
    TileGrid(layout) { index, shape, modifier ->
        val (label, value) = values[index]
        StatTile(
            label = label,
            value = value,
            icon = painterResource(
                when (index) {
                    0 -> R.drawable.ic_pie_chart_filled
                    1 -> R.drawable.ic_military_tech
                    2 -> R.drawable.ic_north_east
                    else -> R.drawable.ic_south_east
                },
            ),
            highlighted = index == 0,
            iconTint = when (index) {
                0 -> null
                1 -> MaterialTheme.colorScheme.onSurfaceVariant
                2 -> MaterialTheme.extendedColors.trendUp
                else -> MaterialTheme.extendedColors.trendDown
            },
            shape = shape,
            modifier = modifier,
        )
    }
}

@Composable
private fun TileGrid(layout: MarketDataLayout, tile: @Composable (index: Int, shape: Shape, modifier: Modifier) -> Unit) {
    if (layout == MarketDataLayout.Row) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(TILE_COUNT) { tile(it, RowTileShapes[it], Modifier.weight(1f)) }
        }
        return
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(2) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) { column -> tile(row * 2 + column, TileShapes[row * 2 + column], Modifier.weight(1f)) }
            }
        }
    }
}
