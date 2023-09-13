package ir.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.composenews.base.BaseViewModel
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.navigation.BottomNavItem
import ir.composenews.navigation.ComposeNewsNavHost
import ir.composenews.navigation.Destinations
import ir.composenews.permission.enum.PermissionType
import ir.composenews.permission.manager.PermissionManager
import ir.composenews.permission.manager.PermissionManagerImpl
import ir.composenews.ui.component.BottomNavigationBar

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PermissionManager by PermissionManagerImpl() {

    private val items = listOf(
        BottomNavItem(
            name = "Markets",
            route = Destinations.MarketListScreen.route,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = "Favorite",
            route = Destinations.FavoriteMarketScreen.route,
            icon = Icons.Default.Favorite,
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performPermission(
            PermissionType.POST_NOTIFICATIONS,
            this
        )

        setContent {
            ComposeNewsTheme {

                // TODO: handle viewModel more properly
                var baseViewModel: BaseViewModel by remember {
                    mutableStateOf(BaseViewModel())
                }
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                val currentScreenRoute = backStackEntry.value?.destination?.route
                val bottomNavVisible = items.any {
                    it.route == currentScreenRoute
                }

                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = bottomNavVisible,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it },
                        ) {
                            BottomNavigationBar(
                                items = items,
                                currentScreenRoute = currentScreenRoute
                            ) {
                                navController.navigate(it.route)
                            }
                        }
                    }
                ) { paddingValues ->
                    ComposeNewsNavHost(
                        navController = navController,
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        onProvideBaseViewModel = { viewModel ->
                            baseViewModel = viewModel
                        }
                    )
                }
            }
        }
    }
}