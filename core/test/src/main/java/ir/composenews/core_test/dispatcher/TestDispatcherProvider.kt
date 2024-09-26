

package ir.composenews.core_test.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider(testScheduler: TestCoroutineScheduler) : DispatcherProvider {
    override val ui: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val bg: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
}
