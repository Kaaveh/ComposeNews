@file:Suppress("ktlint:standard:function-naming")

package ir.composenews.apptv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import ir.composenews.base.errorViewMapper
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.StatePane
import ir.composenews.network.Errors
import java.util.Locale
import androidx.compose.material3.MaterialTheme as PhoneMaterialTheme

private const val SECONDS_PER_MINUTE = 60

/** Nothing stored and the sync failed: a plain error, or a 429 whose `Try again` waits out the countdown. */
@Composable
internal fun TvErrorPane(
    error: Errors,
    retryInSeconds: Int,
    onRefresh: () -> Unit,
    onOpenFavorites: () -> Unit,
) {
    val rateLimited = error is Errors.RateLimited
    val waiting = retryInSeconds > 0
    val colors = PhoneMaterialTheme.colorScheme
    StatePane(
        icon = painterResource(if (rateLimited) R.drawable.ic_hourglass_top else R.drawable.ic_cloud_off),
        iconTint = if (rateLimited) colors.tertiary else colors.error,
        containerColor = if (rateLimited) colors.tertiaryContainer else colors.errorContainer,
        title = errorViewMapper(error),
        body =
            when {
                !rateLimited -> "Prices couldn’t load. Check your connection and try again."
                waiting -> "You can try again when the timer ends."
                else -> "You can try again now."
            },
        illustrationSize = TV_ILLUSTRATION,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TvPaneButton(
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
                icon = painterResource(if (waiting) R.drawable.ic_timer else R.drawable.ic_refresh),
                onClick = onRefresh,
                enabled = !waiting,
            )
            TvPaneButton(
                text = "Open saved favorites",
                icon = painterResource(R.drawable.ic_favorite),
                onClick = onOpenFavorites,
            )
        }
    }
}

/** A pane action as a TV button, with the white focus ring the cards have. */
@Composable
internal fun TvPaneButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors =
            ButtonDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        border = ButtonDefaults.border(focusedBorder = FocusRing.copy(shape = CircleShape)),
        modifier = Modifier.padding(top = 8.dp),
    ) {
        Icon(painter = icon, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        Text(text = text)
    }
}
