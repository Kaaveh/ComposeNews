package ir.kaaveh.composenews.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

abstract class PermissionManagerImpl : PermissionManager {

    override var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun registerPermissionLauncher(activity: ComponentActivity) {
        permissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when {
                    permissions.containsKey(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        readStorageGranted()
                    }
                    permissions.containsKey(Manifest.permission.POST_NOTIFICATIONS) -> {}
                }
            }
    }

    override fun hasNotificationPermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } else {
            true
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun requestNotificationPermission() =
        permissionLauncher?.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))

    override fun hasReadStoragePermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED

    override fun requestReadStoragePermission() =
        permissionLauncher?.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))

    abstract fun readStorageGranted()

}