package ir.composenews.base

import kotlinx.coroutines.flow.StateFlow

interface UnidirectionalViewModel<EVENT, STATE> {
    val state: StateFlow<STATE>

    fun event(event: EVENT)
}
