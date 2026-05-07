@file:Suppress("MagicNumber", "ktlint")

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

@Composable
fun EmptyStateAnimation() {
    val context = LocalContext.current
    val composition by rememberLottieComposition {
        val json = context.resources.openRawResource(R.raw.empty_state_animation)
            .use { it.bufferedReader().readText() }
        LottieCompositionSpec.JsonString(json)
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimationComposable(
            composition = composition,
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
            EmptyStateAnimation()
        }
    }
}
