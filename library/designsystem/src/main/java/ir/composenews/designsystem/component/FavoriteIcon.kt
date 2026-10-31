@file:Suppress("MagicNumber", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.extendedColors

@Composable
fun FavoriteIcon(
    isFavorite: Boolean,
    onFavoriteClick: (() -> Unit)? = null,
) {
    val animationState by remember(isFavorite) { mutableStateOf(isFavorite) }

    val favoriteOn = MaterialTheme.extendedColors.favoriteOn
    val favoriteOff = MaterialTheme.extendedColors.favoriteOff
    val favoriteTransition =
        updateTransition(targetState = animationState, label = "favorite_animation_state")

    val colorTintFavorite by favoriteTransition.animateColor(label = "favorite_color") { state ->
        if (state) favoriteOn else favoriteOff
    }

    val scale by favoriteTransition.animateFloat(
        transitionSpec = {
            if (targetState) {
                keyframes {
                    durationMillis = 300
                    1f at 0
                    1.3f at 150 using FastOutSlowInEasing
                    1f at 300
                }
            } else {
                tween(durationMillis = 300, easing = LinearEasing)
            }
        },
        label = "favorite_scale",
    ) { state ->
        if (state) 1f else 1f
    }

    Icon(
        painter = painterResource(R.drawable.ic_favorite_filled),
        contentDescription = if (isFavorite) "Favorited" else "Not favorited",
        tint = colorTintFavorite,
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .then(if (onFavoriteClick != null) Modifier.clickable { onFavoriteClick() } else Modifier)
            .padding(8.dp),
    )
}

@ThemePreviews
@Composable
private fun FavoriteIconPrev() {
    ComposeNewsTheme {
        Surface {
            Column {
                FavoriteIcon(isFavorite = true)
                FavoriteIcon(isFavorite = false)
            }
        }
    }
}
