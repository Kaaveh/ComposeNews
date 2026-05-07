@file:Suppress("LongParameterList", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
fun LottieAnimationComposable(
    composition: LottieComposition?,
    modifier: Modifier = Modifier,
    iterations: Int = Compottie.IterateForever,
    contentScale: ContentScale = ContentScale.None,
    speed: Float = 1f,
) {
    Image(
        painter = rememberLottiePainter(
            composition = composition,
            iterations = iterations,
            speed = speed,
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
    )
}
