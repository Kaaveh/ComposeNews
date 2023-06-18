package ir.kaaveh.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.kaaveh.composenews.navigation.ComposeNewsNavHost
import ir.kaaveh.composenews.permission.enum.PermissionType
import ir.kaaveh.composenews.permission.manager.PermissionManager
import ir.kaaveh.composenews.permission.manager.PermissionManagerImpl
import ir.kaaveh.composenews.ui.component.BottomNavigationBar
import ir.kaaveh.designsystem.base.BaseRoute
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.designsystem.theme.ComposeNewsTheme
import ir.kaaveh.navigation.BottomNavItem
import ir.kaaveh.navigation.Destinations

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
                    BaseRoute(
                        baseViewModel = baseViewModel
                    ) {
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
}