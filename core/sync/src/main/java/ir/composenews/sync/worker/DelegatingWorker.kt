@file:Suppress(
    "TopLevelPropertyNaming",
    "Indentation",
    "SwallowedException",
    "TooGenericExceptionCaught",
)

package ir.composenews.sync.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

private const val WORKER_CLASS_NAME = "RouterWorkerDelegateClassName"

/**
 * Adds metadata to a WorkRequest to identify what [CoroutineWorker] the [DelegatingWorker] should delegate to
 */
internal fun KClass<out CoroutineWorker>.delegatedData() =
    Data.Builder()
        .putString(WORKER_CLASS_NAME, qualifiedName)
        .build()

/**
 * A worker that delegates sync to another [CoroutineWorker] constructed with Koin dependency injection.
 */
class DelegatingWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val workerClassName =
        workerParams.inputData.getString(WORKER_CLASS_NAME).orEmpty()

    private val delegateWorker = try {
        // Dynamically create worker using Koin
        val workerClass = Class.forName(workerClassName).kotlin
        val worker = workerClass.constructors.first { it.parameters.size == 2 }
            .call(appContext, workerParams) as CoroutineWorker

        worker
    } catch (e: Exception) {
        throw IllegalArgumentException("Unable to find appropriate worker: ${e.message}")
    }

    override suspend fun getForegroundInfo(): ForegroundInfo = delegateWorker.getForegroundInfo()
    override suspend fun doWork(): Result = delegateWorker.doWork()
}
