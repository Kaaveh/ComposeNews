package ir.composenews.testing.dispatcher

import ir.composenews.base.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider(
    val testScheduler: TestCoroutineScheduler = TestCoroutineScheduler(),
) : DispatcherProvider {
    override val ui: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val bg: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
}
