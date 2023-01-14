package ir.kaaveh.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ir.kaaveh.domain.use_case.UpdateNewsUseCase
import ir.kaaveh.sync.SyncConstraints
import ir.kaaveh.sync.syncForegroundInfo

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val updateNewsUseCase: UpdateNewsUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return if (updateNewsUseCase()) Result.success() else Result.retry()
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