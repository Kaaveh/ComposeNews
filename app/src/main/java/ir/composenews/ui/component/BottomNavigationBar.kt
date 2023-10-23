package ir.composenews.ui.component

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.composenews.designsystem.component.BottomNavigationIcon
import ir.composenews.navigation.BottomNavItem
import kotlinx.collections.immutable.PersistentList

@Composable
fun BottomNavigationBar(
    items: PersistentList<BottomNavItem>,
    currentScreenRoute: String?,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        modifier = modifier,
        tonalElevation = 15.dp
    ) {
        items.forEach { item ->
            val selected = item.route == currentScreenRoute
            NavigationBarItem(
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