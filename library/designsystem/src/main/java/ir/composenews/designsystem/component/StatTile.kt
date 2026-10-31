@file:Suppress("MagicNumber", "MaxLineLength", "LongParameterList", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.composenews.designsystem.R
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.designsystem.theme.tabularNums

/** One Market Data value. [highlighted] is the first tile, on the primary container. */
@Composable
fun StatTile(
    label: String,
    value: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    iconTint: Color? = null,
    shape: Shape = RoundedCornerShape(28.dp),
) {
    val containerColor = if (highlighted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val contentColor = if (highlighted) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val labelColor = if (highlighted) contentColor else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .padding(16.dp)
            .semantics(mergeDescendants = true) {},
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(painter = icon, contentDescription = null, tint = iconTint ?: contentColor, modifier = Modifier.size(22.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium),
            color = labelColor,
        )
        val valueStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight(650)).tabularNums()
        Text(
            text = value,
            style = valueStyle,
            color = contentColor,
            maxLines = 1,
            // Four tiles in a row on a tablet, or a narrow phone, shrink a long price instead of cutting it.
            autoSize = TextAutoSize.StepBased(minFontSize = 14.sp, maxFontSize = valueStyle.fontSize),
        )
    }
}

@ThemePreviews
@Composable
private fun StatTilePreview() {
    ComposeNewsTheme {
        Surface {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTile(
                    label = "Market Cap",
                    value = "$2.13T",
                    icon = painterResource(R.drawable.ic_pie_chart_filled),
                    highlighted = true,
                    modifier = Modifier.weight(1f),
                )
                StatTile(
                    label = "High 24h",
                    value = "$108,120",
                    icon = painterResource(R.drawable.ic_north_east),
                    iconTint = MaterialTheme.extendedColors.trendUp,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
