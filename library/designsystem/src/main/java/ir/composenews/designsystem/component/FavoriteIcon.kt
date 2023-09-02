package ir.composenews.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

@Composable
fun FavoriteIcon(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
) {
    Icon(
        imageVector = Icons.Filled.Favorite,
        contentDescription = "",
        tint = when (isFavorite) {
            true -> {
                MaterialTheme.colors.error
            }

            false -> {
                Color.LightGray
            }
        },
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onFavoriteClick() }
            .padding(4.dp)
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