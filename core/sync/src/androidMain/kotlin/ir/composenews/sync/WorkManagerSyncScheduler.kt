package ir.composenews.sync

import android.content.Context
import androidx.startup.AppInitializer
import ir.composenews.sync.initializer.SyncInitializer

internal class WorkManagerSyncScheduler(
    private val context: Context,
) : SyncScheduler {
    override fun schedule() {
        AppInitializer.getInstance(context).initializeComponent(SyncInitializer::class.java)
    }
}
