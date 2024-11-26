@file:Suppress("SwallowedException", "TooGenericExceptionCaught")

package ir.composenews.sync.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import ir.composenews.domain.use_case.SyncMarketListUseCase
import ir.composenews.sync.SyncConstraints
import ir.composenews.sync.syncForegroundInfo

class SyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
    private val syncMarketListUseCase: SyncMarketListUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            syncMarketListUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    companion object {
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}