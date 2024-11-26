@file:Suppress("PackageNaming", "PackageName")

package ir.composenews.core_test.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PlatformDispatcherProvider() : DispatcherProvider {
    override val ui: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val bg: CoroutineDispatcher = Dispatchers.Default
}
