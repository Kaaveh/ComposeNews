@file:Suppress(
    "ktlint:standard:no-empty-first-line-in-class-body",
    "ktlint:standard:function-signature",
    "ktlint:standard:function-expression-body",
    "ktlint:standard:multiline-expression-wrapping",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.base

import androidx.lifecycle.ViewModel
import ir.composenews.base.dispatcher.DispatcherProvider
import ir.composenews.base.dispatcher.PlatformDispatcherProvider
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    protected val dispatcherProvider: DispatcherProvider = PlatformDispatcherProvider(),
) : ViewModel() {

    protected suspend inline fun <T> onUI(crossinline action: suspend () -> T): T {
        return withContext(dispatcherProvider.ui) {
            action()
        }
    }

    protected suspend inline fun <T> onBg(crossinline action: suspend () -> T): T {
        return withContext(dispatcherProvider.bg) {
            action()
        }
    }

    protected suspend inline fun <T> onIO(crossinline action: suspend () -> T): T {
        return withContext(dispatcherProvider.io) {
            action()
        }
    }
}
