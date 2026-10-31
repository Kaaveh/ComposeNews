@file:Suppress("MagicNumber", "LongParameterList", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
                .size(220.dp)
                .background(containerColor, illustrationShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(painter = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(88.dp))
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

/** The Favorites screen with nothing saved. */
@Composable
fun EmptyFavoritesPane(modifier: Modifier = Modifier, actions: @Composable ColumnScope.() -> Unit = {}) {
    StatePane(
        icon = painterResource(R.drawable.ic_favorite_filled),
        iconTint = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        title = "No favorites yet",
        body = "Tap the heart on any market to keep it here, ready at a glance.",
        modifier = modifier,
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
