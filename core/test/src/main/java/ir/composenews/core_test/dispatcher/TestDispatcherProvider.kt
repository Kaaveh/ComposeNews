@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.core_test.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

class TestDispatcherProvider(testScheduler: TestCoroutineScheduler) : DispatcherProvider {
    override val ui: CoroutineDispatcher = StandardTestDispatcher(testScheduler)
    override val io: CoroutineDispatcher = StandardTestDispatcher(testScheduler)
    override val bg: CoroutineDispatcher = StandardTestDispatcher(testScheduler)
}
