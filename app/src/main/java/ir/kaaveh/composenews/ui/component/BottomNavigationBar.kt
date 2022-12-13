package ir.kaaveh.composenews.ui.component

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.kaaveh.composenews.navigation.BottomNavItem
import ir.kaaveh.composenews.navigation.Destinations

val items = listOf(
    BottomNavItem(
        name = "News",
        route = Destinations.NewsListScreen.route,
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        name = "Favorite",
        route = Destinations.FavoriteNewsScreen.route,
        icon = Icons.Default.Favorite,
    ),
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentScreenRoute: String?,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    BottomNavigation(
        modifier = modifier,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == currentScreenRoute
            BottomNavigationItem(
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