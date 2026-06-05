package ir.composenews.core_test.dispatcher

import ir.composenews.base.dispatcher.DispatcherProvider
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider(testScheduler: TestCoroutineScheduler) : DispatcherProvider {
    override val ui: TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val io: TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val bg: TestDispatcher = UnconfinedTestDispatcher(testScheduler)
}
