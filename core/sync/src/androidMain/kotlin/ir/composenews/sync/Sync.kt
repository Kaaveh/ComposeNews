@file:Suppress(
    "ktlint:standard:class-signature",
    "ktlint:standard:function-signature",
    "ktlint:standard:chain-method-continuation",
    "ktlint:standard:multiline-expression-wrapping",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.sync

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType

internal const val SYNC_WORK_NAME = "SyncWorkName"
private const val SYNC_NOTIFICATION_ID = 0
private const val SYNC_NOTIFICATION_CHANNEL_ID = "SyncNotificationChannel"

val SyncConstraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresBatteryNotLow(false)
    .build()

/**
 * Foreground information for sync on lower API levels when sync workers are being
 * run with a foreground service
 */
fun Context.syncForegroundInfo() = ForegroundInfo(
    SYNC_NOTIFICATION_ID,
    syncWorkNotification(),
)

/**
 * Notification displayed on lower API levels when sync workers are being
 * run with a foreground service
 */
private fun Context.syncWorkNotification(): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            SYNC_NOTIFICATION_CHANNEL_ID,
            "sync",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Background tasks for CN"
        }
        // Register the channel with the system
        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.createNotificationChannel(channel)
    }

    // TODO
    return NotificationCompat
        .Builder(this, SYNC_NOTIFICATION_CHANNEL_ID)
//        .setSmallIcon(androidx.hilt.work.R.drawable.notification_action_background)
        .setContentTitle("Background tasks for Compose News")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}
