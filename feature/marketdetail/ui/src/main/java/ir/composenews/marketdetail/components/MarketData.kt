@file:Suppress("MagicNumber", "ktlint")

package ir.composenews.marketdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ir.composenews.base.LoadableData
import ir.composenews.base.errorViewMapper
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.StatTile
import ir.composenews.designsystem.component.formatCompactUsd
import ir.composenews.designsystem.component.formatPrice
import ir.composenews.designsystem.component.SkeletonBlock
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.domain.model.MarketDetail

/** Tiles in reading order; the corner each one turns towards the middle of the grid is 8 dp. */
private val TileShapes = listOf(
    RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomEnd = 8.dp, bottomStart = 28.dp),
    RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomEnd = 28.dp, bottomStart = 8.dp),
    RoundedCornerShape(topStart = 28.dp, topEnd = 8.dp, bottomEnd = 28.dp, bottomStart = 28.dp),
    RoundedCornerShape(topStart = 8.dp, topEnd = 28.dp, bottomEnd = 28.dp, bottomStart = 28.dp),
)

private const val NO_VALUE = "—"

/** `Market Data`: Market Cap, Rank, High 24h and Low 24h in a 2×2 grid. */
@Composable
internal fun MarketData(
    loadableData: LoadableData<MarketDetail>,
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

            is LoadableData.Loaded -> MarketDataTiles(detail = loadableData.data)

            LoadableData.Loading, LoadableData.Initial -> TileGrid { index, modifier ->
                SkeletonBlock(shape = TileShapes[index], modifier = modifier.height(108.dp))
            }
        }
    }
}

@Composable
private fun MarketDataTiles(detail: MarketDetail) {
        val data = detail.marketData
        TileGrid { index, modifier ->
            when (index) {
                0 -> StatTile(
                    label = "Market Cap",
                    value = data?.let { formatCompactUsd(it.marketCapUSD) } ?: NO_VALUE,
                    icon = painterResource(R.drawable.ic_pie_chart_filled),
                    highlighted = true,
                    shape = TileShapes[index],
                    modifier = modifier,
                )

                1 -> StatTile(
                    label = "Rank",
                    value = "#${data?.marketCapRank ?: detail.marketCapRank}",
                    icon = painterResource(R.drawable.ic_military_tech),
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = TileShapes[index],
                    modifier = modifier,
                )

                2 -> StatTile(
                    label = "High 24h",
                    value = data?.let { formatPrice(it.high24hUSD) } ?: NO_VALUE,
                    icon = painterResource(R.drawable.ic_north_east),
                    iconTint = MaterialTheme.extendedColors.trendUp,
                    shape = TileShapes[index],
                    modifier = modifier,
                )

                else -> StatTile(
                    label = "Low 24h",
                    value = data?.let { formatPrice(it.low24hUSD) } ?: NO_VALUE,
                    icon = painterResource(R.drawable.ic_south_east),
                    iconTint = MaterialTheme.extendedColors.trendDown,
                    shape = TileShapes[index],
                    modifier = modifier,
                )
            }
        }
}

@Composable
private fun TileGrid(tile: @Composable (index: Int, modifier: Modifier) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(2) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(2) { column -> tile(row * 2 + column, Modifier.weight(1f)) }
            }
        }
    }
}
