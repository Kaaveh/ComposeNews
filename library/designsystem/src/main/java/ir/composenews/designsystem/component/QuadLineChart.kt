@file:Suppress("MagicNumber", "LongMethod")

package ir.composenews.designsystem.component

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.composenews.designsystem.preview.ThemePreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.designsystem.theme.graphColor
import ir.composenews.designsystem.theme.lightGraphColor
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun QuadLineChart(
    data: List<Pair<Int, Double>> = emptyList(),
) {
    val spacing = 100f
    val columnTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val upperValue = remember(key1 = data) {
        (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt() ?: 0
    }
    val lowerValue = remember(key1 = data) { (data.minOfOrNull { it.second }?.toInt() ?: 0) }
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = columnTextColor
            textAlign = Paint.Align.CENTER
            textSize = density.run { 8.sp.toPx() }
        }
    }

    val animationProgress = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = data) {
        animationProgress.animateTo(1f, tween(3000))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(20.dp),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
        ) {
            val spacePerHour = (size.width - spacing) / data.size
            val priceStep = (upperValue - lowerValue) / 5f

            repeat(5) { i ->
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        round(lowerValue + priceStep * i).toString(),
                        30f,
                        size.height - spacing - i * size.height / 5f,
                        textPaint,
                    )
                }
            }

            var medX: Float
            var medY: Float
            val strokePath = Path().apply {
                val height = size.height
                data.indices.forEach { i ->
                    val nextInfo = data.getOrNull(i + 1) ?: data.last()
                    val firstRatio = (data[i].second - lowerValue) / (upperValue - lowerValue)
                    val secondRatio = (nextInfo.second - lowerValue) / (upperValue - lowerValue)

                    val x1 = spacing + i * spacePerHour
                    val y1 = height - spacing - (firstRatio * height).toFloat()
                    val x2 = spacing + (i + 1) * spacePerHour
                    val y2 = height - spacing - (secondRatio * height).toFloat()
                    if (i == 0) {
                        moveTo(x1, y1)
                    } else {
                        medX = (x1 + x2) / 2f
                        medY = (y1 + y2) / 2f
                        quadraticBezierTo(x1 = x1, y1 = y1, x2 = medX, y2 = medY)
                    }
                }
            }

            val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
                lineTo(size.width - spacePerHour, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

            clipRect(right = size.width * animationProgress.value) {
                drawPath(
                    path = strokePath,
                    color = graphColor,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                    ),
                )

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lightGraphColor,
                            Color.Transparent,
                        ),
                        endY = size.height - spacing,
                    ),
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun QuadLineChartPreview() {
    ComposeNewsTheme {
        Surface {
            QuadLineChart(
                listOf(
                    Pair(0, 5.2),
                    Pair(1, 12.1),
                    Pair(2, 2.3),
                    Pair(3, 6.1),
                    Pair(4, 7.2),
                    Pair(5, 3.0),
                ),
            )
        }
    }
}
