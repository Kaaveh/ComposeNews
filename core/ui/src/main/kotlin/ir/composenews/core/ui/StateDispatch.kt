package ir.composenews.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.composenews.base.UnidirectionalViewModel

data class StateDispatch<EVENT, STATE>(
    val state: STATE,
    val dispatch: (EVENT) -> Unit,
)

@Composable
inline fun <reified EVENT, STATE> use(viewModel: UnidirectionalViewModel<EVENT, STATE>): StateDispatch<EVENT, STATE> {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val dispatch: (EVENT) -> Unit = { event ->
        viewModel.event(event)
    }
    return StateDispatch(
        state = state,
        dispatch = dispatch,
    )
}
