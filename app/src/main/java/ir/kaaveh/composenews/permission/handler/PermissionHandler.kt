package ir.kaaveh.composenews.permission.handler

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ir.kaaveh.composenews.permission.enum.PermissionType

interface PermissionHandler {

    val permission: String
    val permissionType: PermissionType

    fun hasPermission(context: Context): Boolean

    fun permissionLauncher(
        activity: ComponentActivity,
        callback: (isGranted: Boolean) -> Unit,
    ): ActivityResultLauncher<String> = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        callback(isGranted)
    }

    fun requestPermission(
        activity: ComponentActivity,
        callback: (isGranted: Boolean) -> Unit,
    ): Unit? =
        permissionLauncher(activity, callback).launch(permission)

    fun performPermission(
        context: Context,
        activity: ComponentActivity,
        callback: (isGranted: Boolean) -> Unit,
    ) {
        if (hasPermission(context))
            callback(true)
        else
            requestPermission(activity, callback)
    }
}