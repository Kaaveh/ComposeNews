@file:Suppress("TooGenericExceptionCaught", "SwallowedException")

package ir.composenews.sync

import ir.composenews.domain.use_case.SyncMarketListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationWillEnterForegroundNotification

/**
 * Syncs on launch and each time the app comes back to the foreground. iOS has no WorkManager, and
 * `BGTaskScheduler` would need an `Info.plist` entry and the background-modes capability.
 */
internal class ForegroundSyncScheduler(
    private val syncMarketList: SyncMarketListUseCase,
    private val scope: CoroutineScope,
) : SyncScheduler {
    private var job: Job? = null

    override fun schedule() {
        sync()
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationWillEnterForegroundNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
        ) { _ -> sync() }
    }

    /** Skips a sync while one is running. Like `SyncWorker`, a failure waits for the next one. */
    private fun sync() {
        if (job?.isActive == true) return
        job =
            scope.launch {
                try {
                    syncMarketList()
                } catch (e: Exception) {
                    println("Sync failed: $e")
                }
            }
    }
}
