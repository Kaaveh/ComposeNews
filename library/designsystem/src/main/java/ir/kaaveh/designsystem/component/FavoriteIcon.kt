package ir.kaaveh.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ir.kaaveh.designsystem.preview.ThemePreviews
import ir.kaaveh.designsystem.theme.ComposeNewsTheme

@Composable
fun FavoriteIcon(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
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
        modifier = Modifier.clickable { onFavoriteClick() }
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