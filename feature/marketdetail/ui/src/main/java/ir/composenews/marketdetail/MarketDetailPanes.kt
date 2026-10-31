@file:Suppress("MagicNumber", "UnusedPrivateMember", "ktlint")

package ir.composenews.marketdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ir.composenews.core.ui.use
import ir.composenews.designsystem.R
import ir.composenews.designsystem.component.StatePane
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.marketdetail.components.MarketData
import ir.composenews.marketdetail.components.MarketDataLayout

/** A pane this wide (tablet, desktop) gets the large header, the labeled toggle and Market Data in one row. */
private val WidePaneWidth = 560.dp

/** The rounded card a detail pane sits in beside the list. [content] learns whether the pane is wide. */
@Composable
internal fun DetailPaneCard(
    modifier: Modifier = Modifier,
    content: @Composable (wide: Boolean) -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize().padding(top = 16.dp, end = 16.dp, bottom = 16.dp)) {
        val wide = maxWidth >= WidePaneWidth
        // A Surface, so the text on the card gets onSurface in either theme.
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(if (wide) 32.dp else 28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Box(modifier = Modifier.padding(if (wide) 32.dp else 24.dp)) {
                content(wide)
            }
        }
    }
}

/** The detail pane before a market is picked. */
@Composable
fun MarketDetailPlaceholder(modifier: Modifier = Modifier) {
    DetailPaneCard(modifier = modifier) {
        StatePane(
            icon = painterResource(R.drawable.ic_finance_mode),
            iconTint = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            title = "Pick a market",
            body = "Its chart and market data show here.",
        )
    }
}

/** The desktop extra pane's Market Data, read from the same [viewModel] as the detail beside it. */
@Composable
fun MarketDataCard(viewModel: MarketDetailViewModel, modifier: Modifier = Modifier) {
    val (state, _) = use(viewModel = viewModel)
    MarketDataCard(state = state, modifier = modifier)
}

@Composable
internal fun MarketDataCard(state: MarketDetailContract.State, modifier: Modifier = Modifier) {
    ExtraPaneCard(modifier = modifier) {
        MarketData(loadableData = state.marketDetail, layout = MarketDataLayout.List)
    }
}

/** The extra pane's Market Data before a market is picked. */
@Composable
fun MarketDataPlaceholder(modifier: Modifier = Modifier) {
    ExtraPaneCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Market Data",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = "Shows here once you pick a market.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ExtraPaneCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            content()
        }
    }
}

@ThemePreviews
@Composable
private fun MarketDetailPlaceholderPreview() {
    ComposeNewsTheme {
        MarketDetailPlaceholder()
    }
}
