package ir.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.adaptive.calculateDisplayFeatures
import dagger.hilt.android.AndroidEntryPoint
import ir.composenews.base.MainContract
import ir.composenews.permission.enum.PermissionType
import ir.composenews.permission.manager.PermissionManager
import ir.composenews.permission.manager.PermissionManagerImpl
import ir.composenews.designsystem.theme.ComposeNewsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PermissionManager by PermissionManagerImpl() {


    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performPermission(
            PermissionType.POST_NOTIFICATIONS,
            this
        )

        setContent {
            ComposeNewsTheme {

                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val uiState = viewModel.state.collectAsState()

                ComposeNewsApp(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    uiState = uiState.value,
                    closeDetailScreen = { viewModel.closeDetailScreen() },
                    onMarketSelected = { market, contentType ->
                        viewModel.event(
                            MainContract.Event.SetMarket(
                                market = market,
                                contentType = contentType
                            )
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun ComposeNewsAppPreview() {
    ComposeNewsTheme {
        ComposeNewsApp(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
            displayFeatures = emptyList(),
            uiState = MainContract.State(),
            closeDetailScreen = {},
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun ComposeNewsAppPreviewTablet() {
    ComposeNewsTheme {
        ComposeNewsApp(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(700.dp, 500.dp)),
            displayFeatures = emptyList(),
            uiState = MainContract.State(),
            closeDetailScreen = {},
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun ComposeNewsAppPreviewTabletPortrait() {
    ComposeNewsTheme {
        ComposeNewsApp(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp)),
            displayFeatures = emptyList(),
            uiState = MainContract.State(),
            closeDetailScreen = {},
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 1100, heightDp = 600)
@Composable
fun ComposeNewsAppPreviewDesktop() {
    ComposeNewsTheme {
        ComposeNewsApp(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(1100.dp, 600.dp)),
            displayFeatures = emptyList(),
            uiState = MainContract.State(),
            closeDetailScreen = {},
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 600, heightDp = 1100)
@Composable
fun ComposeNewsAppPreviewDesktopPortrait() {
    ComposeNewsTheme {
        ComposeNewsApp(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(600.dp, 1100.dp)),
            displayFeatures = emptyList(),
            uiState = MainContract.State(),
            closeDetailScreen = {},
        )
    }
}