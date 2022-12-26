package ir.kaaveh.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.kaaveh.composenews.navigation.ComposeNewsNavHost
import ir.kaaveh.composenews.ui.component.BottomNavigationBar
import ir.kaaveh.composenews.ui.component.items
import ir.kaaveh.designsystem.base.BaseRoute
import ir.kaaveh.designsystem.base.BaseViewModel
import ir.kaaveh.designsystem.theme.ComposeNewsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO: handle viewModel more properly
    private var baseViewModel: BaseViewModel = BaseViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNewsTheme {

                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                val currentScreenRoute = backStackEntry.value?.destination?.route
                val bottomNavVisible = items.any {
                    it.route == currentScreenRoute
                }

                Scaffold(
                    bottomBar = {
                        if (bottomNavVisible)
                            BottomNavigationBar(
                                items = items,
                                currentScreenRoute = currentScreenRoute
                            ) {
                                navController.navigate(it.route)
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