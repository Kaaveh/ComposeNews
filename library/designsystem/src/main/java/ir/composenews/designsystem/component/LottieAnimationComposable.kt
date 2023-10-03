package ir.composenews.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnimationComposable(
    animationSpec: LottieCompositionSpec,
    modifier: Modifier = Modifier,
    repeatCount: Int = Integer.MAX_VALUE,
    contentScale: ContentScale = ContentScale.None,
    speed: Float = 1f,
    autoPlay: Boolean = true,
) {
    val composition by rememberLottieComposition(spec = animationSpec)

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = repeatCount,
        speed = speed,
        reverseOnRepeat = autoPlay
    )

    LottieAnimation(
        composition = composition, progress = { progress },
        modifier = modifier,
        contentScale = contentScale
    )
}