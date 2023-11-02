@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.core_test

import io.kotest.core.spec.style.FreeSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

/**
 * A Kotest Spec that allows suspension in its initializer. This works great for the
 * `ProjectResource`, for initialising dependencies.
 */
@Suppress("UnnecessaryAbstractClass")
abstract class SuspendSpec(body: suspend FreeSpec.() -> Unit) : FreeSpec() {
    init {
        val scope = autoClose(CloseableCoroutineScope(Dispatchers.Unconfined))
        scope.launch(Dispatchers.Unconfined) { body() }
    }
}

private class CloseableCoroutineScope(context: CoroutineContext) : CoroutineScope, AutoCloseable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = context + job

    override fun close() = runBlocking { job.cancelAndJoin() }
}
