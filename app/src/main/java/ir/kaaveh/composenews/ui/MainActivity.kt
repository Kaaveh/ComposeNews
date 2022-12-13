package ir.kaaveh.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.kaaveh.composenews.navigation.BottomNavItem
import ir.kaaveh.composenews.navigation.ComposeNewsNavHost
import ir.kaaveh.composenews.navigation.Destinations
import ir.kaaveh.composenews.ui.component.BottomNavigationBar
import ir.kaaveh.designsystem.theme.ComposeNewsTheme

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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNewsTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = items,
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) { paddingValues ->
                    ComposeNewsNavHost(
                        navController = navController,
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                    )
                }
            }
        }
    }
}