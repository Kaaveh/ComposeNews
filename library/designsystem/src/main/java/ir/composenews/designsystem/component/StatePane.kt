@file:Suppress("MagicNumber", "LongParameterList", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

/** An empty, error or rate-limited screen: an illustration, a title, a body and optional [actions]. */
@Composable
fun StatePane(
    icon: Painter,
    iconTint: Color,
    containerColor: Color,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    illustrationShape: Shape = CircleShape,
    illustrationSize: Dp = 220.dp,
    actions: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(illustrationSize)
                .background(containerColor, illustrationShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(illustrationSize * 0.4f),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        actions()
    }
}

/**
 * A [StatePane] action: a 56 dp filled button with an icon.
 * The canvas draws `Try again` with 16 dp corners and `Browse markets` as a pill.
 */
@Composable
fun StatePaneButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 28.dp),
        modifier = modifier
            .padding(top = 8.dp)
            .height(56.dp),
    ) {
        Icon(painter = icon, contentDescription = null, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = text)
    }
}

/**
 * Nothing stored and the sync failed: a plain error, or a 429 ([rateLimited]) that is [waiting] out its countdown.
 * The [actions] are the platform's buttons; label the retry one with [formatRetry].
 */
@Composable
fun SyncErrorPane(
    title: String,
    rateLimited: Boolean,
    waiting: Boolean,
    modifier: Modifier = Modifier,
    illustrationSize: Dp = 220.dp,
    actions: @Composable ColumnScope.() -> Unit = {},
) {
    val colors = MaterialTheme.colorScheme
    StatePane(
        icon = painterResource(if (rateLimited) R.drawable.ic_hourglass_top else R.drawable.ic_cloud_off),
        iconTint = if (rateLimited) colors.tertiary else colors.error,
        containerColor = if (rateLimited) colors.tertiaryContainer else colors.errorContainer,
        title = title,
        body = when {
            !rateLimited -> "Prices couldn’t load. Check your connection and try again."
            waiting -> "You can try again when the timer ends."
            else -> "You can try again now."
        },
        modifier = modifier,
        illustrationSize = illustrationSize,
        actions = actions,
    )
}

/** The Favorites screen with nothing saved. */
@Composable
fun EmptyFavoritesPane(
    modifier: Modifier = Modifier,
    illustrationSize: Dp = 220.dp,
    actions: @Composable ColumnScope.() -> Unit = {},
) {
    StatePane(
        icon = painterResource(R.drawable.ic_favorite_filled),
        iconTint = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        title = "No favorites yet",
        body = "Tap the heart on any market to keep it here, ready at a glance.",
        modifier = modifier,
        illustrationSize = illustrationSize,
        actions = actions,
    )
}

@ThemePreviews
@Composable
private fun EmptyFavoritesPanePreview() {
    ComposeNewsTheme {
        Surface {
            EmptyFavoritesPane()
        }
    }
}
