package ir.kaaveh.composenews.permission

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

interface PermissionManager {
    var permissionLauncher: ActivityResultLauncher<Array<String>>?
    fun registerPermissionLauncher(activity: ComponentActivity)

    fun hasNotificationPermission(context: Context) : Boolean
    fun requestNotificationPermission(): Unit?

    fun hasReadStoragePermission(context: Context) : Boolean
    fun requestReadStoragePermission(): Unit?
}