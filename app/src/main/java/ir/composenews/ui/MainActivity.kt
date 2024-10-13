package ir.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.permission.enum.PermissionType
import ir.composenews.permission.manager.PermissionManager
import ir.composenews.permission.manager.PermissionManagerImpl

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PermissionManager by PermissionManagerImpl() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performPermission(
            PermissionType.POST_NOTIFICATIONS,
            this
        )

        setContent {
            ComposeNewsTheme {
                ComposeNewsApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeNewsAppPreview() {
    ComposeNewsTheme {
        ComposeNewsApp(
        )
    }
}

@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun ComposeNewsAppPreviewTablet() {
    ComposeNewsTheme {
        ComposeNewsApp(
        )
    }
}

@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun ComposeNewsAppPreviewTabletPortrait() {
    ComposeNewsTheme {
        ComposeNewsApp(
        )
    }
}

@Preview(showBackground = true, widthDp = 1100, heightDp = 600)
@Composable
fun ComposeNewsAppPreviewDesktop() {
    ComposeNewsTheme {
        ComposeNewsApp(
        )
    }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 1100)
@Composable
fun ComposeNewsAppPreviewDesktopPortrait() {
    ComposeNewsTheme {
        ComposeNewsApp(
        )
    }
}