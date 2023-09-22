@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.core_test

import ir.composenews.core_test.dispatcher.TestDispatcherProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals

interface TestObserver<T> {
    val observed: List<T>
}

class FlowTestObserver<T>(
    scope: TestScope,
    testDispatcherProvider: TestDispatcherProvider,
    flow: Flow<T>,
) : TestObserver<T> {

    private val result = mutableListOf<T>()
    override val observed: List<T> = result

    private val job: Job = scope.launch(testDispatcherProvider.io) {
        flow.collect { result.add(it) }
    }

    fun assertNoValues(): TestObserver<T> {
        assertEquals(emptyList<T>(), observed)
        return this
    }

    fun assertValues(vararg values: T): TestObserver<T> {
        assertEquals(values.toList(), observed)
        return this
    }

    fun finish() {
        job.cancel()
    }
}

fun <T> Flow<T>.test(
    scope: TestScope,
    testDispatcherProvider: TestDispatcherProvider,
): FlowTestObserver<T> {
    return FlowTestObserver(scope, testDispatcherProvider, this)
}
