package ir.composenews.base

import androidx.lifecycle.ViewModel
import ir.composenews.core_test.dispatcher.DispatcherProvider
import ir.composenews.core_test.dispatcher.PlatformDispatcherProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

open class BaseViewModel(
    protected val dispatcherProvider: DispatcherProvider = PlatformDispatcherProvider(),
) : ViewModel(), BaseContract {

    protected val mutableBaseState: MutableStateFlow<BaseContract.BaseState> =
        MutableStateFlow(BaseContract.BaseState.OnSuccess)
    override val baseState: StateFlow<BaseContract.BaseState> = mutableBaseState.asStateFlow()

    protected val baseEffectChannel = Channel<BaseContract.BaseEffect>(Channel.UNLIMITED)
    override val baseEffect: Flow<BaseContract.BaseEffect> = baseEffectChannel.receiveAsFlow()

    override fun baseEvent(event: BaseContract.BaseEvent) = when (event) {
        BaseContract.BaseEvent.OnBackPressed -> onBackPressed()
        BaseContract.BaseEvent.OnRetryPressed -> onRetryPressed()
    }

    private fun onBackPressed() {
        baseEffectChannel.trySend(BaseContract.BaseEffect.OnBackPressed)
    }

    private fun onRetryPressed() = Unit

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
