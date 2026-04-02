package ir.composenews.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey

data class BottomNavItem(
    val name: String,
    val route: NavKey,
    val icon: ImageVector,
    val badgeCount: Int = 0,
)
