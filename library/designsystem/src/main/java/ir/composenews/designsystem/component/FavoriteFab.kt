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
import androidx.compose.foundation.shape.RoundedCornerShape
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
            .semantics {
                stateDescription = if (isFavorite) "In favorites" else "Not in favorites"
                onClick(label = if (isFavorite) "Remove from favorites" else "Add to favorites", action = null)
            },
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

@ThemePreviews
@Composable
private fun FavoriteFabPreview() {
    ComposeNewsTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                FavoriteFab(isFavorite = false, onClick = {})
                Spacer(Modifier.height(16.dp))
                FavoriteFab(isFavorite = true, onClick = {})
            }
        }
    }
}
