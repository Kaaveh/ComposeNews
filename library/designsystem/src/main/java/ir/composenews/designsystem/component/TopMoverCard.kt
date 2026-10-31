@file:Suppress("MagicNumber", "LongParameterList", "LongMethod", "MaxLineLength", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.designsystem.theme.tabularNums
import java.util.Locale

/** The biggest 24h gainer of the synced markets, above the list. */
@Composable
fun TopMoverCard(
    id: String,
    name: String,
    symbol: String,
    imageUrl: String,
    price: String,
    change: Double,
    sparkline: List<Double>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .semantics { onClick(label = "Open $name details", action = null) },
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_bolt_filled),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(text = "Top mover in top 250 · 24h", style = MaterialTheme.typography.labelLarge)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                MarketLogo(id = id, imageUrl = imageUrl, size = 56.dp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = symbol.uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                    )
                    Text(text = name, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                }
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight(600)).tabularNums(),
                        maxLines = 1,
                    )
                    val trend = if (change >= 0) MaterialTheme.extendedColors.trendUp else MaterialTheme.extendedColors.trendDown
                    TrendChange(
                        change = change,
                        color = MaterialTheme.colorScheme.surface,
                        iconSize = 18.dp,
                        modifier = Modifier
                            .background(trend, CircleShape)
                            .padding(start = 6.dp, top = 2.dp, end = 10.dp, bottom = 2.dp),
                    )
                }
            }
            Sparkline(
                points = sparkline,
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.5.dp,
                fillAlpha = 0.12f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
            )
        }
    }
}

@ThemePreviews
@Composable
private fun TopMoverCardPreview() {
    ComposeNewsTheme {
        Surface {
            TopMoverCard(
                id = "solana",
                name = "Solana",
                symbol = "sol",
                imageUrl = "",
                price = "$187.42",
                change = 12.8,
                sparkline = listOf(150.0, 155.0, 152.0, 165.0, 172.0, 169.0, 187.0),
                onClick = {},
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
