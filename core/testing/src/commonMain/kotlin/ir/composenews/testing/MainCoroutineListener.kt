package ir.composenews.testing

import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/** Pass the scheduler of the spec's `TestDispatcherProvider` so Main and its dispatchers share virtual time. */
class MainCoroutineListener(
    testScheduler: TestCoroutineScheduler = TestCoroutineScheduler(),
) : BeforeSpecListener,
    AfterSpecListener {
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    override suspend fun beforeSpec(spec: Spec) {
        Dispatchers.setMain(testDispatcher)
    }

    override suspend fun afterSpec(spec: Spec) {
        Dispatchers.resetMain()
    }
}
