@file:Suppress("MagicNumber", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.extendedColors

/** A price line scaled to its own min and max. Draws nothing with fewer than two points. */
@Composable
fun Sparkline(
    points: List<Double>,
    color: Color,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 1.75.dp,
    fillAlpha: Float = 0f,
) {
    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas
        val min = points.min()
        val range = (points.max() - min).takeIf { it > 0.0 } ?: 1.0
        val inset = strokeWidth.toPx() / 2
        val height = size.height - inset * 2
        val stepX = size.width / (points.size - 1)
        val line = Path()
        points.forEachIndexed { i, price ->
            val x = i * stepX
            val y = inset + height - ((price - min) / range * height).toFloat()
            if (i == 0) line.moveTo(x, y) else line.lineTo(x, y)
        }
        if (fillAlpha > 0f) {
            val area = Path().apply {
                addPath(line)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(area, color.copy(alpha = fillAlpha))
        }
        drawPath(
            path = line,
            color = color,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}

@ThemePreviews
@Composable
private fun SparklinePreview() {
    ComposeNewsTheme {
        Surface {
            Sparkline(
                points = listOf(5.0, 6.2, 5.8, 7.1, 6.9, 8.4),
                color = MaterialTheme.extendedColors.trendUp,
                modifier = Modifier.size(52.dp, 22.dp),
            )
        }
    }
}
