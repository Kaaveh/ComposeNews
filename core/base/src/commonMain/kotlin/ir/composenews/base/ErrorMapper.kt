@file:Suppress(
    "TopLevelPropertyNaming",
    "Indentation",
    "ktlint:standard:function-expression-body",
    "ktlint:standard:trailing-comma-on-call-site"
)

package ir.composenews.base

import ir.composenews.network.Errors

fun errorViewMapper(errors: Errors): String {
    return when (errors) {
        is Errors.ApiError -> {
            errors.message ?: "Unknown Error"
        }

        is Errors.ExceptionError -> {
            errors.message ?: "Unknown Error"
        }
    }
}
