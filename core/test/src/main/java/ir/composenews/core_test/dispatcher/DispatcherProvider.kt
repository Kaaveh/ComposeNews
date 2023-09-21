@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.core_test.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
interface DispatcherProvider {
    val ui: CoroutineDispatcher
    val io: CoroutineDispatcher
    val bg: CoroutineDispatcher
}
