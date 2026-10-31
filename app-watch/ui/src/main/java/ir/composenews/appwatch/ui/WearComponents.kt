@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.appwatch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import ir.composenews.designsystem.component.TrendChange
import ir.composenews.designsystem.component.formatRetry

/** The default scroll indicator, left out of scroll captures (long screenshots). */
@Composable
fun wearScrollIndicator(state: TransformingLazyColumnState): (@Composable BoxScope.() -> Unit)? =
    if (LocalScrollCaptureInProgress.current) null else ({ ScrollIndicator(state = state) })

/** The design system's trend change, in the watch's label type: an arrow and the size, read out as `Up 2.14 %`. */
@Composable
fun WearTrendChange(
    change: Double,
    modifier: Modifier = Modifier,
) {
    TrendChange(
        change = change,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
        iconSize = 14.dp,
        modifier = modifier,
    )
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
            label = { Text(text = formatRetry(retryInSeconds)) },
        )
    }
}

/**
 * The end of the list: `Refresh`, which a watch has no pull gesture for, and why the last sync failed while the
 * stored markets stay on screen. Disabled while syncing or while a 429 runs out.
 */
@Composable
fun WearRefreshContent(
    syncError: String?,
    isRefreshing: Boolean,
    retryInSeconds: Int,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val waiting = retryInSeconds > 0
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        syncError?.let {
            Text(
                text = "Couldn’t refresh: $it",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = onRefresh,
            enabled = !isRefreshing && !waiting,
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
                        when {
                            isRefreshing -> "Refreshing…"
                            waiting -> formatRetry(retryInSeconds)
                            else -> "Refresh"
                        },
                )
            },
        )
    }
}
