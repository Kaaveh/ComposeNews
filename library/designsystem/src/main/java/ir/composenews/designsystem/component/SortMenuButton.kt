@file:Suppress("MagicNumber", "LongParameterList", "LongMethod", "MaxLineLength", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme

/**
 * An icon button that opens a `Sort by` menu. It is a rounded square while closed and a circle while open;
 * whether it is open is UI state, so it lives here.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> SortMenuButton(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String,
    icon: @Composable (T) -> Painter,
    modifier: Modifier = Modifier,
    contentDescription: String = "Sort markets",
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(modifier = modifier) {
        FilledIconToggleButton(
            checked = expanded,
            onCheckedChange = { expanded = it },
            modifier = Modifier.size(48.dp),
            shapes = IconButtonDefaults.toggleableShapes(
                shape = RoundedCornerShape(16.dp),
                checkedShape = CircleShape,
            ),
            colors = IconButtonDefaults.filledIconToggleButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        ) {
            Icon(painter = painterResource(R.drawable.ic_swap_vert), contentDescription = contentDescription)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp),
            )
            options.forEach { option ->
                val isSelected = option == selected
                DropdownMenuItem(
                    text = {
                        Text(text = label(option), fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                    leadingIcon = { Icon(painter = icon(option), contentDescription = null, modifier = Modifier.size(20.dp)) },
                    trailingIcon = if (isSelected) {
                        { Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null) }
                    } else {
                        null
                    },
                    colors = if (isSelected) {
                        MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            leadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            trailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    } else {
                        MenuDefaults.itemColors()
                    },
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .then(if (isSelected) Modifier.background(MaterialTheme.colorScheme.secondaryContainer) else Modifier)
                        .semantics { this.selected = isSelected },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SortMenuButtonPreview() {
    ComposeNewsTheme {
        Surface {
            var sort by remember { mutableStateOf("Market cap") }
            SortMenuButton(
                options = listOf("Market cap", "Volume"),
                selected = sort,
                onSelect = { sort = it },
                label = { it },
                icon = { painterResource(if (it == "Volume") R.drawable.ic_bar_chart else R.drawable.ic_pie_chart) },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
