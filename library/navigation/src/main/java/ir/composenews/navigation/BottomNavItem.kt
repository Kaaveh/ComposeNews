package ir.composenews.navigation

import androidx.annotation.DrawableRes
import androidx.navigation3.runtime.NavKey

data class BottomNavItem(
    val name: String,
    val route: NavKey,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val badgeCount: Int = 0,
)
