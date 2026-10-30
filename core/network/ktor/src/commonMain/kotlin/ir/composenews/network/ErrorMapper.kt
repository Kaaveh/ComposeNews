@file:Suppress(
    "ktlint"
)
package ir.composenews.network

import io.ktor.http.HttpHeaders

/** CoinGecko doesn't always send `Retry-After`; its limit is per minute. */
private const val DEFAULT_RETRY_AFTER_SECONDS = 60

fun ApiResponse.Failure.Error.toErrors(): Errors {
    val status = statusCode
    return if (status == StatusCode.TooManyRequests) {
        val retryAfter = payloadResponse.headers[HttpHeaders.RetryAfter]?.toIntOrNull()
        Errors.RateLimited(retryAfter ?: DEFAULT_RETRY_AFTER_SECONDS)
    } else {
        Errors.ApiError(status.mapMessageStatusCode(), status.code)
    }
}

fun StatusCode.mapMessageStatusCode(): String {
    return when (this) {
        StatusCode.Unknown -> "Unknown"
        StatusCode.BadRequest -> "Bad Request"
        StatusCode.Forbidden -> "Forbidden"
        StatusCode.RequestTimeout -> "Request Timeout"
        StatusCode.TooManyRequests -> "Too many Requests"
        StatusCode.InternalServerError -> "Internal Server Error"
        StatusCode.ServiceUnavailable -> "Service Unavailable"
        StatusCode.AccessDenied -> "Access Denied"
        StatusCode.LimitRequest -> "Limit Request"
    }
}
