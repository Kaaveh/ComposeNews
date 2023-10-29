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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.accompanist.adaptive.calculateDisplayFeatures
import dagger.hilt.android.AndroidEntryPoint
import ir.composenews.designsystem.preview.DevicesPreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.navigation.MainContract
import ir.composenews.permission.enum.PermissionType
import ir.composenews.permission.manager.PermissionManager
import ir.composenews.permission.manager.PermissionManagerImpl
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

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
                    displayFeatures = displayFeatures.toPersistentList(),
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
@DevicesPreviews()
@Composable
fun ComposeNewsAppPreview() {
    ComposeNewsTheme {
        ComposeNewsApp(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
            displayFeatures = persistentListOf(),
            uiState = MainContract.State(),
            closeDetailScreen = {},
        )
    }
}
