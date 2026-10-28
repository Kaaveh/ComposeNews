@file:Suppress("ktlint")

package ir.composenews.core.ui

import androidx.compose.runtime.Composable
import ir.composenews.base.LoadableData
import ir.composenews.network.Errors

@Composable
fun <T> LoadableComponent(
    loadableData: LoadableData<T>,
    loading: @Composable () -> Unit = {},
    error: @Composable (Errors) -> Unit,
    loaded: @Composable (T) -> Unit,
    initial: @Composable () -> Unit = {},
) {
    when (loadableData) {
        is LoadableData.Loading -> loading()
        is LoadableData.Error -> error(loadableData.error)
        is LoadableData.Loaded -> loaded(loadableData.data)
        is LoadableData.Initial -> initial()
    }
}
