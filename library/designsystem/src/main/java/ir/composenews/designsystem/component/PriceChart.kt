@file:Suppress("MagicNumber", "LongMethod", "UnusedPrivateMember", "ktlint")

package ir.composenews.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.extendedColors
import ir.composenews.designsystem.theme.tabularNums
import java.util.Locale
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.roundToInt

private const val GRID_LINES = 3

/**
 * The detail price line, with dashed gridlines, price labels on the right and a scrub marker.
 * The marker starts on the latest price; a tap or a horizontal drag moves it, unless [scrubbable] is false
 * (Wear, where a horizontal swipe is the back gesture).
 */
@Composable
fun PriceChart(
    prices: List<Pair<Long, Double>>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.extendedColors.chartLine,
    height: Dp = 188.dp,
    scrubbable: Boolean = true,
) {
    if (prices.size < 2) {
        Box(modifier = modifier.fillMaxWidth().height(height), contentAlignment = Alignment.Center) {
            Text(text = "No chart data", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }

    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelSmall.tabularNums()
        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val markerFill = MaterialTheme.colorScheme.surface
    val min = remember(prices) { prices.minOf { it.second } }
    val max = remember(prices) { prices.maxOf { it.second } }
    val (low, span) = remember(min, max) { axisRange(min, max) }
    val labels = remember(low, span, labelStyle) {
        val decimals = axisDecimals(span / (GRID_LINES + 1))
        List(GRID_LINES) { i ->
            val fraction = (i + 1.0) / (GRID_LINES + 1)
            textMeasurer.measure(String.format(Locale.US, "%,.${decimals}f", low + span * (1 - fraction)), labelStyle)
        }
    }
    var scrubIndex by remember(prices) { mutableIntStateOf(prices.lastIndex) }
    val reveal = remember(prices) { Animatable(0f) }
    LaunchedEffect(prices) { reveal.animateTo(1f, tween(1000)) }

    // The labels sit in a gutter as wide as the widest one, so big prices or font scales don't overlap the line
    val gutter = labels.maxOf { it.size.width } + with(LocalDensity.current) { 8.dp.roundToPx() }
    // size is read per event, so a resized chart maps touches to the right point
    fun PointerInputScope.scrubTo(x: Float) {
        scrubIndex = scrubIndexAt(x, (size.width - gutter).toFloat(), prices.size)
    }
    val scrub = if (scrubbable) {
        Modifier
            .pointerInput(prices, gutter) { detectTapGestures { scrubTo(it.x) } }
            .pointerInput(prices, gutter) {
                detectHorizontalDragGestures(onDragStart = { scrubTo(it.x) }) { change, _ ->
                    scrubTo(change.position.x)
                }
            }
    } else {
        Modifier
    }
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .semantics { contentDescription = "Price chart, ${formatPrice(min)} to ${formatPrice(max)}" }
            .then(scrub),
    ) {
        val plotWidth = size.width - gutter
        val markerRadius = 7.dp.toPx()
        val top = markerRadius
        val plotHeight = size.height - markerRadius * 2
        fun xAt(i: Int) = i * plotWidth / prices.lastIndex
        fun yAt(price: Double) = top + plotHeight - ((price - low) / span * plotHeight).toFloat()

        labels.forEachIndexed { i, label ->
            val fraction = (i + 1f) / (GRID_LINES + 1)
            val y = top + plotHeight * fraction
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(plotWidth, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 4.dp.toPx())),
            )
            drawText(
                textLayoutResult = label,
                topLeft = Offset(size.width - label.size.width, y - label.size.height / 2f),
            )
        }

        val line = Path()
        prices.forEachIndexed { i, (_, price) ->
            if (i == 0) line.moveTo(xAt(i), yAt(price)) else line.lineTo(xAt(i), yAt(price))
        }
        val area = Path().apply {
            addPath(line)
            lineTo(plotWidth, size.height)
            lineTo(0f, size.height)
            close()
        }

        clipRect(right = plotWidth * reveal.value) {
            drawPath(area, Brush.verticalGradient(listOf(lineColor.copy(alpha = 0.5f), Color.Transparent)))
            drawPath(
                path = line,
                color = lineColor,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }

        if (reveal.value == 1f) {
            val marker = Offset(xAt(scrubIndex), yAt(prices[scrubIndex].second))
            drawLine(
                color = lineColor.copy(alpha = 0.35f),
                start = Offset(marker.x, 0f),
                end = Offset(marker.x, size.height),
                strokeWidth = 1.dp.toPx(),
            )
            drawCircle(color = markerFill, radius = markerRadius, center = marker)
            drawCircle(color = lineColor, radius = markerRadius, center = marker, style = Stroke(width = 3.dp.toPx()))
        }
    }
}

/**
 * The axis bottom and height for prices from [min] to [max]. A flat series gets a band of ±1% around its price
 * (1 wide at zero), so the line sits mid-chart and the labels stay near the real price.
 */
internal fun axisRange(min: Double, max: Double): Pair<Double, Double> {
    if (max > min) return min to max - min
    val span = if (min == 0.0) 1.0 else abs(min) * 0.02
    return min - span / 2 to span
}

/** Enough decimals that axis labels [step] apart never round to the same text. */
internal fun axisDecimals(step: Double): Int = (1 - floor(log10(step))).toInt().coerceIn(0, 8)

/** The index of the point nearest to [x] on a plot [plotWidth] wide holding [count] points. */
internal fun scrubIndexAt(x: Float, plotWidth: Float, count: Int): Int =
    if (count < 2 || plotWidth <= 0f) 0 else (x / plotWidth * (count - 1)).roundToInt().coerceIn(0, count - 1)

private fun formatPrice(value: Double): String = when {
    value >= 1_000 -> String.format(Locale.US, "%,.0f", value)
    value >= 1 -> String.format(Locale.US, "%.2f", value)
    else -> String.format(Locale.US, "%.6f", value).trimEnd('0').trimEnd('.')
}

@ThemePreviews
@Composable
private fun PriceChartPreview() {
    ComposeNewsTheme {
        Surface {
            PriceChart(
                prices = listOf(5.2, 6.1, 5.7, 7.2, 6.8, 8.3, 7.9).mapIndexed { i, price -> i.toLong() to price },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@ThemePreviews
@Composable
private fun PriceChartEmptyPreview() {
    ComposeNewsTheme {
        Surface {
            PriceChart(prices = emptyList())
        }
    }
}
