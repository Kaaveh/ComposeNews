package ir.composenews.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationIcon(
    name: String,
    icon: ImageVector,
    selected: Boolean,
    badgeCount: Int,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (badgeCount > 0) {
            BadgedBox(
                badge = {
                    Text(text = badgeCount.toString())
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = name
            )
        }
        if (selected) {
            Text(
                text = name,
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
    }
}