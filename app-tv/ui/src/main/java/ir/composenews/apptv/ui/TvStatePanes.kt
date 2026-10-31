@file:Suppress("LongParameterList", "ktlint:standard:function-naming")

package ir.composenews.apptv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import ir.composenews.designsystem.component.SyncErrorPane
import ir.composenews.designsystem.component.formatRetry
import ir.composenews.network.Errors

/** Nothing stored and the sync failed: a plain error, or a 429 whose `Try again` waits out the countdown. */
@Composable
internal fun TvErrorPane(
    error: Errors,
    retryInSeconds: Int,
    onRefresh: () -> Unit,
    onOpenFavorites: () -> Unit,
    takeFocus: Boolean,
    onFocusTaken: () -> Unit,
) {
    val waiting = retryInSeconds > 0
    // A disabled TV button keeps focus, so `Try again` takes it even while it counts down.
    val tryAgain = remember { FocusRequester() }
    RequestFocusWhen(takeFocus = takeFocus, requester = tryAgain, onFocusTaken = onFocusTaken)
    SyncErrorPane(
        title = errorViewMapper(error),
        rateLimited = error is Errors.RateLimited,
        waiting = waiting,
        illustrationSize = TV_ILLUSTRATION,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TvPaneButton(
                text = formatRetry(retryInSeconds),
                icon = painterResource(if (waiting) R.drawable.ic_timer else R.drawable.ic_refresh),
                onClick = onRefresh,
                enabled = !waiting,
                modifier = Modifier.focusRequester(tryAgain),
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
    modifier: Modifier = Modifier,
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
        modifier = modifier.padding(top = 8.dp),
    ) {
        Icon(painter = icon, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        Text(text = text)
    }
}

/**
 * Focuses [requester] while [takeFocus] is set: at launch, and after a button removed itself (a retry, a tab switch
 * from a pane, the last favorite removed). Compose would otherwise hand focus to the tabs.
 */
@Composable
internal fun RequestFocusWhen(
    takeFocus: Boolean,
    requester: FocusRequester,
    onFocusTaken: () -> Unit,
) {
    LaunchedEffect(requester, takeFocus) {
        if (takeFocus) {
            // One frame, so the requester is attached and laid out.
            withFrameNanos {}
            requester.requestFocus()
            onFocusTaken()
        }
    }
}
