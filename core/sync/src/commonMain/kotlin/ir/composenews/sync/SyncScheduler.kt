package ir.composenews.sync

/** Starts the background market sync. Android runs it on WorkManager. */
interface SyncScheduler {
    fun schedule()
}
