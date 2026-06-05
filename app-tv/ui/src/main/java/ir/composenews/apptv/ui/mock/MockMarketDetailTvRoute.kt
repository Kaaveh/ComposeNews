@file:Suppress("ktlint")

package ir.composenews.apptv.ui.mock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ir.composenews.uimarket.model.MarketModel

@Composable
fun MockMarketDetailTvRoute(market: MarketModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = market.imageUrl),
                contentDescription = market.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
            )
            Column {
                Text(
                    text = market.name,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = market.symbol.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        HorizontalDivider()

        MockStatRow(label = "Price", value = "$ ${market.currentPrice}")
        MockStatRow(
            label = "24h change",
            value = "${"%.2f".format(market.priceChangePercentage24h)} %",
        )
        MockStatRow(label = "Favorite", value = if (market.isFavorite) "Yes" else "No")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Showing mocked data — emulator has no network.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MockStatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Text(text = value, style = MaterialTheme.typography.titleMedium)
    }
}
