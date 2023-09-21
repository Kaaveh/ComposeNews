package ir.composenews.sync

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.startup.AppInitializer
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import ir.composenews.sync.initializer.SyncInitializer

object Sync {
    fun init(context: Context) = AppInitializer.getInstance(context)
        .initializeComponent(SyncInitializer::class.java)
}

internal const val SyncWorkName = "SyncWorkName"
private const val SyncNotificationId = 0
private const val SyncNotificationChannelID = "SyncNotificationChannel"

val SyncConstraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresBatteryNotLow(false)
    .build()

/**
 * Foreground information for sync on lower API levels when sync workers are being
 * run with a foreground service
 */
fun Context.syncForegroundInfo() = ForegroundInfo(
    SyncNotificationId,
    syncWorkNotification(),
)

/**
 * Notification displayed on lower API levels when sync workers are being
 * run with a foreground service
 */
private fun Context.syncWorkNotification(): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            SyncNotificationChannelID,
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

    return NotificationCompat
        .Builder(this, SyncNotificationChannelID)
        .setSmallIcon(androidx.hilt.work.R.drawable.notification_action_background)
        .setContentTitle("Background tasks for Compose News")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}
