package ir.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScreenScaffold
import ir.composenews.MainViewModel
import ir.composenews.appwatch.navigation.MainContract
import ir.composenews.appwatch.navigation.graph.Destinations
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.navigation.ComposeNewsWearNavHost
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }

    @Composable
    @Suppress("ktlint:standard:function-naming")
    fun WearApp() {
        val navController = rememberSwipeDismissableNavController()
        val state = mainViewModel.state.collectAsStateWithLifecycle()
        ComposeNewsTheme {
            AppScaffold {
                ScreenScaffold(timeText = { TimeText() }) {
                    ComposeNewsWearNavHost(
                        navController = navController,
                        onMarketSelected = {
                            mainViewModel.event(MainContract.Event.SetMarket(it))
                            navController.navigate(Destinations.MarketDetailScreen().route)
                        },
                        uiState = state.value,
                        modifier = Modifier,
                    )
                }
            }
        }
    }

    @Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
    @Composable
    @Suppress("ktlint:standard:function-naming")
    fun DefaultPreview() {
        WearApp()
    }
}
