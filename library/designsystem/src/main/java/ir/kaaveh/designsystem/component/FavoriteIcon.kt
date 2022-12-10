package ir.kaaveh.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

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
                Color.Red
            }
            false -> {
                Color.LightGray
            }
        },
        modifier = Modifier.clickable { onFavoriteClick() }
    )
}

@Preview
@Composable
private fun FavoriteIconPrev() {
    Column {
        FavoriteIcon(isFavorite = true) {}
        FavoriteIcon(isFavorite = false) {}
    }
}