@file:Suppress("MagicNumber", "LongParameterList", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

/**
 * A single-choice row of connected toggle buttons: the selected one morphs into a full pill.
 * Serves the market filters (48 dp, with icons) and the chart ranges (40 dp, on [uncheckedContainerColor]).
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> ConnectedButtonGroup(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    icon: (@Composable (T) -> Painter)? = null,
    height: Dp = 48.dp,
    uncheckedContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        options.forEachIndexed { index, option ->
            ToggleButton(
                checked = option == selected,
                onCheckedChange = { onSelect(option) },
                modifier = Modifier
                    .weight(1f)
                    .height(height)
                    .semantics { role = Role.RadioButton },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                colors = ToggleButtonDefaults.colors(
                    containerColor = uncheckedContainerColor,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            ) {
                if (icon != null) {
                    Icon(painter = icon(option), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                }
                Text(text = label(option), style = MaterialTheme.typography.labelLarge, maxLines = 1)
            }
        }
    }
}

@ThemePreviews
@Composable
private fun ConnectedButtonGroupPreview() {
    ComposeNewsTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                var filter by remember { mutableStateOf("All") }
                ConnectedButtonGroup(
                    options = listOf("All", "Gainers", "Losers"),
                    selected = filter,
                    onSelect = { filter = it },
                    label = { it },
                    icon = {
                        painterResource(
                            when (it) {
                                "Gainers" -> R.drawable.ic_trending_up
                                "Losers" -> R.drawable.ic_trending_down
                                else -> R.drawable.ic_apps
                            },
                        )
                    },
                )
                Spacer(Modifier.height(16.dp))
                var range by remember { mutableStateOf("1D") }
                ConnectedButtonGroup(
                    options = listOf("1D", "1W", "1M", "1Y"),
                    selected = range,
                    onSelect = { range = it },
                    label = { it },
                    height = 40.dp,
                    uncheckedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                )
            }
        }
    }
}
