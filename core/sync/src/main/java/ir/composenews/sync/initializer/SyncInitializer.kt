@file:Suppress(
    "ktlint:standard:class-signature",
    "ktlint:standard:function-signature",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.sync.initializer

import android.content.Context
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import ir.composenews.sync.SYNC_WORK_NAME
import ir.composenews.sync.Sync
import ir.composenews.sync.worker.SyncWorker

class SyncInitializer : Initializer<Sync> {
    override fun create(context: Context): Sync {
        WorkManager.getInstance(context).apply {
            // Run sync on app startup and ensure only one sync worker runs at any time
            enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                SyncWorker.startUpSyncWork(),
            )
        }

        return Sync
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
