@file:Suppress("MagicNumber", "ktlint")

package ir.composenews.network

/**
 * https://docs.coingecko.com/reference/common-errors-rate-limit
 */
enum class StatusCode(val code: Int) {
    Unknown(0),
    BadRequest(400),
    Forbidden(403),
    RequestTimeout(408),
    TooManyRequests(429),
    InternalServerError(500),
    ServiceUnavailable(500),
    AccessDenied(500),
    LimitRequest(10005),
}
