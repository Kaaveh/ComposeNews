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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.WindowInfo
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
import kotlinx.coroutines.flow.first

/** Nothing stored and the sync failed: a plain error, or a 429 whose `Try again` waits out the countdown. */
@Composable
internal fun TvErrorPane(
    error: Errors,
    retryInSeconds: Int,
    onRefresh: () -> Unit,
    onOpenFavorites: () -> Unit,
    retryModifier: Modifier = Modifier,
) {
    val waiting = retryInSeconds > 0
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
                // A disabled TV button keeps focus, so `Try again` takes it even while it counts down.
                modifier = retryModifier,
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
 * Keeps focus on the screen while it changes under it. When a focused button goes away, Compose drops focus and
 * Android hands it to the top-most focusable, the tabs. So a button that removes itself (a retry, a tab switch from
 * a pane, the last favorite) [park]s focus on a spot just below the tabs, and whatever the screen shows next takes it
 * from there through [entry]. It starts parked, so the screen also takes focus at launch and after a recreation.
 */
@Stable
internal class ContentFocus {
    /** The first card, or the pane's button. */
    val entry = FocusRequester()
    private val spot = FocusRequester()
    private var parked by mutableStateOf(true)

    /**
     * For an empty spot below the tabs, focusable only while parked. Not the whole content: a focus target that
     * reaches up behind the tabs isn't below them, so Down from the tabs would skip all of it.
     */
    val parkingSpot: Modifier = Modifier.focusRequester(spot).focusProperties { canFocus = parked }.focusTarget()

    fun park() {
        parked = true
        spot.requestFocus()
    }

    /** Hands parked focus to [entry]; called each time the screen shows something new. */
    suspend fun takeFocus(window: WindowInfo) {
        if (!parked) return
        // Android focuses the top-most view, the tabs, when the window gets focus, so wait for that first.
        snapshotFlow { window.isWindowFocused }.first { it }
        // And one frame, so the new entry is attached and laid out.
        withFrameNanos {}
        if (entry.requestFocus(FocusDirection.Enter)) parked = false
    }
}
