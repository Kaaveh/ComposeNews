package ir.composenews.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

@Composable
fun FavoriteIcon(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    val animatedColor by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colors.error else Color.LightGray,
        animationSpec = tween(500),
        label = "color",
    )

    Icon(
        imageVector = Icons.Filled.Favorite,
        contentDescription = "",
        tint = animatedColor,
        modifier = Modifier
            .clickable { onFavoriteClick() },
    )
}

@ThemePreviews
@Composable
private fun FavoriteIconPrev() {
    ComposeNewsTheme {
        Surface {
            Column {
                FavoriteIcon(isFavorite = true) {}
                FavoriteIcon(isFavorite = false) {}
            }
        }
    }
}