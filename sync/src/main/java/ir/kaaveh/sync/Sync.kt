package ir.kaaveh.sync

import android.content.Context
import androidx.startup.AppInitializer
import androidx.work.*
import ir.kaaveh.sync.initializer.SyncInitializer

object Sync {
    fun init(context: Context) = AppInitializer.getInstance(context)
        .initializeComponent(SyncInitializer::class.java)
}

internal const val SyncWorkName = "SyncWorkName"

val SyncConstraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .setRequiresBatteryNotLow(false)
    .build()


