package ir.composenews.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackgroundSwipe(modifier: Modifier, dismissState: DismissState) {
    val color by animateColorAsState(
        targetValue = when (dismissState.targetValue) {
            DismissValue.DismissedToStart -> if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                Color.Red
            }
            else -> if (isSystemInDarkTheme().not()) Color.LightGray else Color.Gray
        },
        label = "",
    )

    val iconScale by animateFloatAsState(
        targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.1f,
        label = "",
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 5.dp)
                .scale(iconScale),
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
        )
    }
}
