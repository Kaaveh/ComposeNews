package ir.kaaveh.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
//import ir.kaaveh.domain.use_case.SyncNewsUseCase
import ir.kaaveh.sync.SyncConstraints
import ir.kaaveh.sync.syncForegroundInfo

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
//    private val syncNewsUseCase: SyncNewsUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
//        return if (syncNewsUseCase()) Result.success() else Result.retry()
        return Result.success()
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