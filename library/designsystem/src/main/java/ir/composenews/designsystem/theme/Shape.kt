@file:Suppress(
    "ktlint"
)

package ir.composenews.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(12.dp),
)

private val GroupOuter = 24.dp
private val GroupInner = 6.dp

/** A grouped list's row at [index] of [count]: 24 dp on the group's outer corners, 6 dp between rows. */
fun groupedRowShape(index: Int, count: Int): RoundedCornerShape {
    val top = if (index == 0) GroupOuter else GroupInner
    val bottom = if (index == count - 1) GroupOuter else GroupInner
    return RoundedCornerShape(topStart = top, topEnd = top, bottomEnd = bottom, bottomStart = bottom)
}
