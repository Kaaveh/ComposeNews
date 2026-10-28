@file:Suppress(
    "ktlint"
)

package ir.composenews.network

sealed class Errors {
    data class ApiError(val message: String?, val code: Int) : Errors()

    data class ExceptionError(val message: String?, val throwable: Throwable? = null) : Errors()
}
