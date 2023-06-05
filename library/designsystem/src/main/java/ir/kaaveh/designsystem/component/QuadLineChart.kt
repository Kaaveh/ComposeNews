package ir.kaaveh.designsystem.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun QuadLineChart(
    data: List<Pair<Int, Double>> = emptyList(),
) {
    val spacing = 100f
    val graphColor = Color(0xFF6750A4)
    val transparentGraphColor = remember { graphColor.copy(alpha = 0.5f) }
    val upperValue = remember { (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt() ?: 0 }
    val lowerValue = remember { (data.minOfOrNull { it.second }?.toInt() ?: 0) }
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 8.sp.toPx() }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(20.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val spacePerHour = (size.width - spacing) / data.size
//            (data.indices step 2).forEach { i ->
//                val hour = data[i].first
//                drawContext.canvas.nativeCanvas.apply {
//                    drawText(
//                        hour.toString(),
//                        spacing + i * spacePerHour,
//                        size.height,
//                        textPaint
//                    )
//                }
//            }
//
            val priceStep = (upperValue - lowerValue) / 5f
            (0..4).forEach { i ->
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        round(lowerValue + priceStep * i).toString(),
                        30f,
                        size.height - spacing - i * size.height / 5f,
                        textPaint
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

            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
                lineTo(size.width - spacePerHour, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        transparentGraphColor,
                        Color.Transparent
                    ),
                    endY = size.height - spacing
                )
            )

        }

    }
}

// TODO: Implement preview