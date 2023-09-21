package ir.composenews.designsystem.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

@Composable
fun ErrorView(errorMessage: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 20.dp),
        )
    }
}

@ThemePreviews
@Composable
private fun ErrorViewPrev() {
    ComposeNewsTheme {
        ErrorView(errorMessage = "An unknown error happened!")
    }
}
