package ir.kaaveh.composenews.permission.handler

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import ir.kaaveh.composenews.permission.enum.PermissionType

class NotificationPermissionHandler : PermissionHandler {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override val permission: String = Manifest.permission.POST_NOTIFICATIONS

    override val permissionType: PermissionType = PermissionType.POST_NOTIFICATIONS

    override fun hasPermission(
        context: Context
    ): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } else {
            true
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun permissionLauncher(
        activity: ComponentActivity,
        callback: (isGranted: Boolean) -> Unit,
    ): ActivityResultLauncher<String> = super.permissionLauncher(activity, callback)


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun requestPermission(
        activity: ComponentActivity,
        callback: (isGranted: Boolean) -> Unit,
    ): Unit? =
        super.requestPermission(activity, callback)
}