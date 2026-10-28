@file:Suppress(
    "TooGenericExceptionCaught",
    "ThrowingExceptionsWithoutMessageOrCause",
    "MagicNumber",
    "ktlint"
)

package ir.composenews.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

val successCode: IntRange = 200..299

fun HttpResponse.getStatusCode(): StatusCode {
    return StatusCode.entries.find {
        it.code == status.value
    } ?: StatusCode.Unknown
}

val ApiResponse.Failure.Error.payloadResponse: HttpResponse
    inline get() = (payload as? HttpResponse) ?: throw IllegalArgumentException()

val ApiResponse.Failure.Error.statusCode: StatusCode
    inline get() = payloadResponse.getStatusCode()

suspend inline fun <reified T> apiResponseOf(
    successCodeRange: IntRange = successCode,
    crossinline f: suspend () -> HttpResponse,
): ApiResponse<T> = try {
    val response = f()
    if (response.status.value in successCodeRange) {
        ApiResponse.Success(
            data = response.body() ?: Unit as T,
        )
    } else {
        ApiResponse.Failure.Error(response)
    }
} catch (ex: Exception) {
    ApiResponse.Failure.Exception(ex)
}
