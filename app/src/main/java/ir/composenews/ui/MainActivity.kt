package ir.composenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import ir.composenews.designsystem.preview.DevicesPreviews
import ir.composenews.designsystem.theme.ComposeNewsTheme
import ir.composenews.permission.enum.PermissionType
import ir.composenews.permission.manager.PermissionManager
import ir.composenews.permission.manager.PermissionManagerImpl

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    PermissionManager by PermissionManagerImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        performPermission(
            PermissionType.POST_NOTIFICATIONS,
            this,
        )

        setContent {
            ComposeNewsTheme {
                ComposeNewsApp()
            }
        }
    }
}

@DevicesPreviews
@Composable
@Suppress("ktlint:standard:function-naming", "UnusedPrivateMember")
private fun ComposeNewsAppPreview() {
    ComposeNewsTheme {
        ComposeNewsApp()
    }
}
