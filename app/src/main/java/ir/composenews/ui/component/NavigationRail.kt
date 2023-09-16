package ir.composenews.ui.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.component.BottomNavigationIcon
import ir.composenews.navigation.BottomNavItem

@Composable
fun ComposeNewsNavigationRail(
    items: List<BottomNavItem>,
    currentScreenRoute: String?,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight()
            .padding(end = 30.dp),
    ) {
        items.forEach { item ->
            val selected = item.route == currentScreenRoute
            NavigationRailItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    BottomNavigationIcon(
                        name = item.name,
                        icon = item.icon,
                        selected = selected,
                        badgeCount = item.badgeCount,
                    )
                }
            )
        }
    }
}