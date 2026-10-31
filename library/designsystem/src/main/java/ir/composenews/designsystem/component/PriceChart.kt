@file:Suppress("MagicNumber", "LongMethod", "UnusedPrivateMember", "TooManyFunctions", "ktlint")

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
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
 * The marker starts on the latest price; a tap, a horizontal drag or a mouse hover moves it and shows a tooltip
 * with that point's price and time, unless [scrubbable] is false (Wear, where a horizontal swipe is the back gesture).
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
    var scrubbed by remember(prices) { mutableStateOf(false) }
    val tooltipColor = MaterialTheme.colorScheme.inverseSurface
    val tooltip = rememberTooltip(prices, scrubIndex, scrubbed, textMeasurer)
    val reveal = remember(prices) { Animatable(0f) }
    LaunchedEffect(prices) { reveal.animateTo(1f, tween(1000)) }

    // The labels sit in a gutter as wide as the widest one, so big prices or font scales don't overlap the line
    val gutter = labels.maxOf { it.size.width } + with(LocalDensity.current) { 8.dp.roundToPx() }
    // size is read per event, so a resized chart maps touches to the right point
    fun PointerInputScope.scrubTo(x: Float) {
        scrubIndex = scrubIndexAt(x, (size.width - gutter).toFloat(), prices.size)
        scrubbed = true
    }
    val scrub = if (scrubbable) {
        Modifier
            .pointerInput(prices, gutter) { detectTapGestures { scrubTo(it.x) } }
            .pointerInput(prices, gutter) {
                detectHorizontalDragGestures(onDragStart = { scrubTo(it.x) }) { change, _ ->
                    scrubTo(change.position.x)
                }
            }
            .onMouseHover(prices, gutter, onMove = { scrubTo(it) }, onExit = { scrubbed = false })
    } else {
        Modifier
    }
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .semantics {
                contentDescription = "Price chart, ${formatPrice(min)} to ${formatPrice(max)}"
                tooltip?.let { stateDescription = it.label }
            }
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

            tooltip?.let { drawTooltip(it, marker, markerRadius, plotWidth, tooltipColor) }
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

/**
 * Calls [onMove] with x as a mouse hovers and [onExit] as it leaves; touch moves are left to the drag,
 * so a vertical scroll doesn't scrub.
 */
private fun Modifier.onMouseHover(
    key1: Any?,
    key2: Any?,
    onMove: PointerInputScope.(x: Float) -> Unit,
    onExit: () -> Unit,
): Modifier =
    pointerInput(key1, key2) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val change = event.changes.first()
                if (change.type != PointerType.Mouse) continue
                when (event.type) {
                    PointerEventType.Move -> this@pointerInput.onMove(change.position.x)
                    PointerEventType.Exit -> onExit()
                }
            }
        }
    }

/** The scrubbed point's price and time, measured, and [label] for screen readers. */
private class Tooltip(val label: String, val price: TextLayoutResult, val time: TextLayoutResult)

/** The tooltip for [prices] at [index], formatted and measured once per point, or null until [scrubbed]. */
@Composable
private fun rememberTooltip(
    prices: List<Pair<Long, Double>>,
    index: Int,
    scrubbed: Boolean,
    textMeasurer: TextMeasurer,
): Tooltip? {
    val priceStyle = MaterialTheme.typography.labelLarge.tabularNums()
        .copy(color = MaterialTheme.colorScheme.inverseOnSurface, fontWeight = FontWeight.SemiBold)
    val timeStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.inverseOnSurface)
    return remember(prices, index, scrubbed, priceStyle, timeStyle) {
        if (!scrubbed) return@remember null
        val (millis, value) = prices[index]
        val price = formatUsdPrice(value)
        val time = formatChartTime(millis)
        Tooltip("$price at $time", textMeasurer.measure(price, priceStyle), textMeasurer.measure(time, timeStyle))
    }
}

/** The scrubbed point's price over its time, in a box above the [marker], or below it near the top. */
private fun DrawScope.drawTooltip(
    tooltip: Tooltip,
    marker: Offset,
    markerRadius: Float,
    plotWidth: Float,
    color: Color,
) {
    val (price, time) = tooltip.price to tooltip.time
    val padH = 12.dp.toPx()
    val padV = 8.dp.toPx()
    val box = Size(
        width = maxOf(price.size.width, time.size.width) + padH * 2,
        height = (price.size.height + time.size.height) + padV * 2,
    )
    val left = tooltipLeft(marker.x, box.width, plotWidth)
    val top = tooltipTop(marker.y, markerRadius + 4.dp.toPx(), box.height, size.height)
    drawRoundRect(color = color, topLeft = Offset(left, top), size = box, cornerRadius = CornerRadius(12.dp.toPx()))
    drawText(price, topLeft = Offset(left + padH, top + padV))
    drawText(time, topLeft = Offset(left + padH, top + padV + price.size.height))
}

/**
 * The tooltip's top edge: the top of the chart, unless the marker at [markerY] would sit under it, then just
 * below the marker ([clearance] away), kept inside a chart [chartHeight] tall.
 */
internal fun tooltipTop(markerY: Float, clearance: Float, height: Float, chartHeight: Float): Float =
    if (markerY - clearance >= height) 0f else (markerY + clearance).coerceAtMost(chartHeight - height)

/** The tooltip's left edge: centered on the marker at [x], kept inside a plot [plotWidth] wide. */
internal fun tooltipLeft(x: Float, width: Float, plotWidth: Float): Float =
    (x - width / 2).coerceAtMost(plotWidth - width).coerceAtLeast(0f)

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
