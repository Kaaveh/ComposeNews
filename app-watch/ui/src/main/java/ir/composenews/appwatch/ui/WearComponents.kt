@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.appwatch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalScrollCaptureInProgress
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScrollIndicator
import androidx.wear.compose.material3.Text
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.formatPercent
import ir.composenews.designsystem.theme.LocalExtendedColors
import java.util.Locale

private const val SECONDS_PER_MINUTE = 60

/** The default scroll indicator, left out of scroll captures (long screenshots). */
@Composable
fun wearScrollIndicator(state: TransformingLazyColumnState): (@Composable BoxScope.() -> Unit)? =
    if (LocalScrollCaptureInProgress.current) null else ({ ScrollIndicator(state = state) })

/** An arrow and the size of the change in the trend color, so the trend never relies on color alone. */
@Composable
fun WearTrendChange(
    change: Double,
    modifier: Modifier = Modifier,
) {
    val color = if (change >= 0) LocalExtendedColors.current.trendUp else LocalExtendedColors.current.trendDown
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(if (change >= 0) R.drawable.ic_arrow_upward else R.drawable.ic_arrow_downward),
            contentDescription = if (change >= 0) "Up" else "Down",
            tint = color,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = formatPercent(change),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = color,
            maxLines = 1,
        )
    }
}

/** A failed load: the message and `Try again`, disabled with a countdown while a 429 runs out. */
@Composable
fun WearErrorContent(
    message: String,
    retryInSeconds: Int,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val waiting = retryInSeconds > 0
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        Button(
            onClick = onRetry,
            enabled = !waiting,
            modifier = Modifier.fillMaxWidth(),
            icon = {
                Icon(
                    painter = painterResource(if (waiting) R.drawable.ic_timer else R.drawable.ic_refresh),
                    contentDescription = null,
                )
            },
            label = {
                Text(
                    text =
                        if (waiting) {
                            String.format(
                                Locale.US,
                                "Try again in %d:%02d",
                                retryInSeconds / SECONDS_PER_MINUTE,
                                retryInSeconds % SECONDS_PER_MINUTE,
                            )
                        } else {
                            "Try again"
                        },
                )
            },
        )
    }
}
