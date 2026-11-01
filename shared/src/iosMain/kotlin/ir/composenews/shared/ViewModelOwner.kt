package ir.composenews.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import platform.Foundation.NSThread
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

/**
 * Keeps one screen's ViewModel alive for a Swift owner, so Swift never calls `ViewModelStore.put`.
 * [clear] runs `onCleared()` and cancels `viewModelScope` on the main thread, whichever thread the owner deinits on.
 */
class ViewModelOwner(
    viewModel: ViewModel,
) {
    private val store = ViewModelStore().apply { put(KEY, viewModel) }

    fun clear() {
        if (NSThread.isMainThread) store.clear() else dispatch_async(dispatch_get_main_queue()) { store.clear() }
    }

    private companion object {
        const val KEY = "viewModel"
    }
}
