@file:Suppress(
    "SwallowedException",
    "TooGenericExceptionCaught",
    "ktlint:standard:annotation",
    "ktlint:standard:function-expression-body",
    "ktlint:standard:function-signature",
    "ktlint:standard:multiline-expression-wrapping",
)

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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams),
    KoinComponent {
    private val syncMarketListUseCase: SyncMarketListUseCase by inject()

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
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .build()
    }
}
