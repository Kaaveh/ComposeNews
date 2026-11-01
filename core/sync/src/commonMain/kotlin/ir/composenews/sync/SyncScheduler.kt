package ir.composenews.sync

/**
 * Starts the background market sync. Android runs it on WorkManager; iOS syncs when the app comes to
 * the foreground.
 */
interface SyncScheduler {
    fun schedule()
}
