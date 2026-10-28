@file:Suppress("ktlint")

package ir.composenews.network

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T> ApiResponse<T>.onSuccess(
    crossinline onResult: ApiResponse.Success<T>.() -> Unit,
): ApiResponse<T> {
    contract { callsInPlace(onResult, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResponse.Success) {
        onResult(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <T> ApiResponse<T>.suspendOnSuccess(
    crossinline onResult: suspend ApiResponse.Success<T>.() -> Unit,
): ApiResponse<T> {
    contract { callsInPlace(onResult, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResponse.Success) {
        onResult(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <T, V> ApiResponse.Success<T>.suspendMap(
    crossinline mapper: suspend (ApiResponse.Success<T>) -> V,
): V {
    contract { callsInPlace(mapper, InvocationKind.AT_MOST_ONCE) }
    return mapper(this)
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <T> ApiResponse<T>.suspendOnError(
    crossinline onResult: suspend ApiResponse.Failure.Error.() -> Unit,
): ApiResponse<T> {
    contract { callsInPlace(onResult, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResponse.Failure.Error) {
        onResult(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> ApiResponse<T>.onError(
    crossinline onResult: ApiResponse.Failure.Error.() -> Unit,
): ApiResponse<T> {
    contract { callsInPlace(onResult, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResponse.Failure.Error) {
        onResult(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <V> ApiResponse.Failure.Error.suspendMap(
    crossinline mapper: suspend (ApiResponse.Failure.Error) -> V,
): V {
    contract { callsInPlace(mapper, InvocationKind.AT_MOST_ONCE) }
    return mapper(this)
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <T> ApiResponse<T>.suspendOnException(
    crossinline onResult: suspend ApiResponse.Failure.Exception.() -> Unit,
): ApiResponse<T> {
    contract { callsInPlace(onResult, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResponse.Failure.Exception) {
        onResult(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> ApiResponse<T>.onException(
    crossinline onResult: ApiResponse.Failure.Exception.() -> Unit,
): ApiResponse<T> {
    contract { callsInPlace(onResult, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResponse.Failure.Exception) {
        onResult(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <V> ApiResponse.Failure.Exception.suspendMap(
    crossinline mapper: suspend (ApiResponse.Failure.Exception) -> V,
): V {
    contract { callsInPlace(mapper, InvocationKind.AT_MOST_ONCE) }
    return mapper(this)
}
