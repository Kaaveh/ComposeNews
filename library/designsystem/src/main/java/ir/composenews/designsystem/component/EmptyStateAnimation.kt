@file:Suppress("MagicNumber")

package ir.composenews.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

@Composable
fun EmptyStateAnimation(
    lottieCompositionSpec: LottieCompositionSpec,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimationComposable(
            animationSpec = lottieCompositionSpec,
            modifier = Modifier
                .size(250.dp, 250.dp)
                .scale(
                    0.5f,
                    0.5f,
                ),
        )
        Text(
            modifier = Modifier.padding(bottom = 18.dp),
            text = "Your favorite list is empty",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
        )
    }
}

@ThemePreviews
@Composable
fun EmptyStateAnimationPrev() {
    ComposeNewsTheme {
        Surface {
            EmptyStateAnimation(
                lottieCompositionSpec = LottieCompositionSpec.RawRes(R.raw.empty_state_animation),
            )
        }
    }
}
