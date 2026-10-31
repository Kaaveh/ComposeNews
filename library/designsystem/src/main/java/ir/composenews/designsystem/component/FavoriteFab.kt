@file:Suppress("MagicNumber", "MaxLineLength", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

/**
 * `Favorite` on the primary container; once saved, `Saved` on primary with rounder corners.
 * Screen readers hear whether the market is saved and what a tap will do, as with the list's heart.
 */
@Composable
fun FavoriteFab(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        label = "favorite_fab_container",
    )
    val contentColor by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
        label = "favorite_fab_content",
    )
    val corner by animateDpAsState(targetValue = if (isFavorite) 20.dp else 16.dp, label = "favorite_fab_corner")
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .height(64.dp)
            .favoriteSemantics(isFavorite),
        shape = RoundedCornerShape(corner),
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Icon(
            painter = painterResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite),
            contentDescription = null,
            modifier = Modifier.size(26.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = if (isFavorite) "Saved" else "Favorite",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}

/**
 * The favorite toggle in a detail pane's header, where a FAB would cover the pane: the FAB's colors on a 56 dp button.
 * Without [showLabel] (a narrow pane) only the heart shows, and it carries the `Remove {name} from favorites` description.
 */
@Composable
fun FavoriteToggleButton(
    isFavorite: Boolean,
    marketName: String,
    onClick: () -> Unit,
    showLabel: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .then(if (showLabel) Modifier else Modifier.width(56.dp))
            .favoriteSemantics(isFavorite),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        contentPadding = if (showLabel) PaddingValues(start = 18.dp, end = 22.dp) else PaddingValues(0.dp),
    ) {
        Icon(
            painter = painterResource(if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite),
            contentDescription = when {
                showLabel -> null
                isFavorite -> "Remove $marketName from favorites"
                else -> "Add $marketName to favorites"
            },
            modifier = Modifier.size(24.dp),
        )
        if (showLabel) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (isFavorite) "Saved" else "Favorite",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

/** Screen readers hear whether the market is saved and what a tap will do, as with the list's heart. */
private fun Modifier.favoriteSemantics(isFavorite: Boolean): Modifier = semantics {
    stateDescription = if (isFavorite) "In favorites" else "Not in favorites"
    onClick(label = if (isFavorite) "Remove from favorites" else "Add to favorites", action = null)
}

@ThemePreviews
@Composable
private fun FavoriteFabPreview() {
    ComposeNewsTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                FavoriteFab(isFavorite = false, onClick = {})
                Spacer(Modifier.height(16.dp))
                FavoriteFab(isFavorite = true, onClick = {})
                Spacer(Modifier.height(16.dp))
                FavoriteToggleButton(isFavorite = true, marketName = "Bitcoin", onClick = {}, showLabel = true)
                Spacer(Modifier.height(16.dp))
                FavoriteToggleButton(isFavorite = false, marketName = "Bitcoin", onClick = {}, showLabel = false)
            }
        }
    }
}
