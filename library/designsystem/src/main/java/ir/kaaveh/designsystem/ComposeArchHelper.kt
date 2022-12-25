package ir.kaaveh.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

data class StateEffectDispatch<STATE, EFFECT, EVENT>(
    val state: STATE,
    val effectFlow: Flow<EFFECT>,
    val dispatch: (EVENT) -> Unit,
)

data class StateDispatch<STATE, EVENT>(
    val state: STATE,
    val dispatch: (EVENT) -> Unit,
)

@Composable
inline fun <reified STATE, EFFECT, EVENT> useWithEffect(
    viewModel: UnidirectionalViewModelWithEffect<EVENT, EFFECT, STATE>,
): StateEffectDispatch<STATE, EFFECT, EVENT> {
    val state by viewModel.state.collectAsState()

    val dispatch: (EVENT) -> Unit = { event ->
        viewModel.event(event)
    }
    return StateEffectDispatch(
        state = state,
        effectFlow = viewModel.effect,
        dispatch = dispatch
    )
}

@Composable
inline fun <reified STATE, EVENT> use(
    viewModel: UnidirectionalViewModel<EVENT, STATE>,
): StateDispatch<STATE, EVENT> {
    val state by viewModel.state.collectAsState()

    val dispatch: (EVENT) -> Unit = { event ->
        viewModel.event(event)
    }
    return StateDispatch(
        state = state,
        dispatch = dispatch
    )
}

@Composable
inline fun <reified BASE_STATE, BASE_EFFECT, BASE_EVENT> useBase(
    viewModel: BaseUnidirectionalViewModel<BASE_EVENT, BASE_EFFECT, BASE_STATE>,
): StateEffectDispatch<BASE_STATE, BASE_EFFECT, BASE_EVENT> {
    val state by viewModel.baseState.collectAsState()

    val dispatch: (BASE_EVENT) -> Unit = { event ->
        viewModel.baseEvent(event)
    }
    return StateEffectDispatch(
        state = state,
        effectFlow = viewModel.baseEffect,
        dispatch = dispatch
    )
}

interface UnidirectionalViewModelWithEffect<EVENT, EFFECT, STATE> {
    val state: StateFlow<STATE>
    val effect: Flow<EFFECT>
    fun event(event: EVENT)
}

interface UnidirectionalViewModel<EVENT, STATE> {
    val state: StateFlow<STATE>
    fun event(event: EVENT)
}

interface BaseUnidirectionalViewModel<BASE_EVENT, BASE_EFFECT, BASE_STATE> {
    val baseState: StateFlow<BASE_STATE>
    val baseEffect: Flow<BASE_EFFECT>
    fun baseEvent(event: BASE_EVENT)
}

@Suppress("ComposableNaming")
@Composable
fun <T> Flow<T>.collectInLaunchedEffect(function: suspend (value: T) -> Unit) {
    val flow = this
    LaunchedEffect(key1 = flow) {
        flow.collectLatest(function)
    }
}