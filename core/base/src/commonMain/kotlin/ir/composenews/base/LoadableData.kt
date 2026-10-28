@file:Suppress(
    "ktlint:standard:class-signature",
    "ktlint:standard:function-signature",
)

package ir.composenews.base

import ir.composenews.network.Errors

sealed class LoadableData<out T> {
    abstract val data: T?

    data object Initial : LoadableData<Nothing>() {
        override val data = null
    }

    data object Loading : LoadableData<Nothing>() {
        override val data = null
    }

    data class Loaded<T>(override val data: T) : LoadableData<T>()

    data class Error(val error: Errors) : LoadableData<Nothing>() {
        override val data = null
    }
}

val LoadableData<*>.isLoading: Boolean
    get() = this is LoadableData.Loading
