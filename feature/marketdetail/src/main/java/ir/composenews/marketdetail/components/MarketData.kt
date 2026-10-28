@file:Suppress("MagicNumber", "ktlint")

package ir.composenews.marketdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.composenews.core.ui.LoadableComponent
import ir.composenews.base.LoadableData
import ir.composenews.designsystem.component.shimmerEffect
import ir.composenews.domain.model.MarketDetail
import ir.composenews.marketdetail.formatNumber

@Composable
internal fun MarketData(
    loadableData: LoadableData<MarketDetail>,
) {
    LoadableComponent(
        loading = {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .clip(RoundedCornerShape(4.dp))
                        .height(24.dp)
                        .shimmerEffect(),
                )
            }
            HorizontalDivider(color = Color.Gray)
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 8.dp),
            ) {
                repeat(4) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(20.dp)
                                .shimmerEffect(),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .clip(RoundedCornerShape(4.dp))
                                .height(16.dp)
                                .shimmerEffect(),
                        )
                    }
                }
            }
        },
        loaded = { data ->
            MarketDataHeader()
            MarketDetail(data.marketData)
            Spacer(modifier = Modifier.height(80.dp))
        },
        error = { error -> },
        loadableData = loadableData,
    )
}

@Composable
private fun MarketDataHeader() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Market Data",
            style = MaterialTheme.typography.titleLarge,
        )
    }
    HorizontalDivider(color = Color.Gray)
}

@Composable
private fun MarketDetail(
    marketData: MarketDetail.MarketData?,
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MarketCap(marketCapUSD = marketData?.marketCapUSD ?: 0)
        High24(high24hUSD = marketData?.high24hUSD ?: 0.0)
        Low24(low24hUSD = marketData?.low24hUSD ?: 0.0)
        Rank(marketCapRank = marketData?.marketCapRank ?: 0)
    }
}

@Composable
private fun Rank(marketCapRank: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Rank",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "#$marketCapRank",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun Low24(low24hUSD: Double) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Low 24h",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = low24hUSD.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun High24(high24hUSD: Double) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "High 24h",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = high24hUSD.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun MarketCap(marketCapUSD: Long) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Market Cap",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = formatNumber(marketCapUSD),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
