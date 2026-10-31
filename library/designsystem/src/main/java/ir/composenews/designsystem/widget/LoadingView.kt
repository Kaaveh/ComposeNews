@file:Suppress("UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        ContainedLoadingIndicator(
            modifier = Modifier.align(Alignment.Center),
            indicatorColor = MaterialTheme.colorScheme.primary,
        )
    }
}

@ThemePreviews
@Composable
private fun LoadingViewPreview() {
    ComposeNewsTheme {
        Surface {
            LoadingView()
        }
    }
}
