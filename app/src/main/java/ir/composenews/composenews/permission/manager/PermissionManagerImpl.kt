package ir.composenews.composenews.permission.manager

import android.content.Context
import androidx.activity.ComponentActivity
import ir.composenews.composenews.permission.enum.PermissionType
import ir.composenews.composenews.permission.handler.NotificationPermissionHandler
import ir.composenews.composenews.permission.handler.PermissionHandler
import ir.composenews.composenews.permission.handler.ReadStoragePermissionHandler

class PermissionManagerImpl : PermissionManager {
    private fun getPermissionHandler(
        permission: PermissionType
    ): PermissionHandler =
        when (permission) {
            PermissionType.POST_NOTIFICATIONS -> NotificationPermissionHandler()
            PermissionType.READ_EXTERNAL_STORAGE -> ReadStoragePermissionHandler()
        }

    override fun hasPermission(
        permission: PermissionType,
        context: Context
    ): Boolean =
        getPermissionHandler(permission).hasPermission(context)

    override fun requestPermission(
        permission: PermissionType,
        activity: ComponentActivity,
        callback: (isGranted: Boolean) -> Unit,
    ): Unit? =
        getPermissionHandler(permission).requestPermission(activity, callback)


    override fun performPermission(
        permission: PermissionType,
        activity: ComponentActivity,
        callback: (isGranted: Boolean) -> Unit,
    ) = getPermissionHandler(permission).performPermission(activity, activity, callback)
}