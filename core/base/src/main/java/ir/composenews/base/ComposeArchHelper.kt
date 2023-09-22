package ir.composenews.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

data class StateEffectDispatch<EVENT, EFFECT, STATE>(
    val state: STATE,
    val effectFlow: Flow<EFFECT>,
    val dispatch: (EVENT) -> Unit,
)

data class StateDispatch<EVENT, STATE>(
    val state: STATE,
    val dispatch: (EVENT) -> Unit,
)

@Composable
inline fun <reified EVENT, STATE> use(
    viewModel: UnidirectionalViewModel<EVENT, STATE>,
): StateDispatch<EVENT, STATE> {
    val state by viewModel.state.collectAsState()

    val dispatch: (EVENT) -> Unit = { event ->
        viewModel.event(event)
    }
    return StateDispatch(
        state = state,
        dispatch = dispatch,
    )
}

@Composable
inline fun <reified BASE_EVENT, BASE_EFFECT, BASE_STATE> useBase(
    viewModel: BaseUnidirectionalViewModel<BASE_EVENT, BASE_EFFECT, BASE_STATE>,
): StateEffectDispatch<BASE_EVENT, BASE_EFFECT, BASE_STATE> {
    val state by viewModel.baseState.collectAsStateWithLifecycle()

    val dispatch: (BASE_EVENT) -> Unit = { event ->
        viewModel.baseEvent(event)
    }
    return StateEffectDispatch(
        state = state,
        effectFlow = viewModel.baseEffect,
        dispatch = dispatch,
    )
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
