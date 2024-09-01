package ir.composenews.network

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.HttpMethod

suspend inline fun <reified T> HttpClient.getApiResponse(
    builder: HttpRequestBuilder,
): ApiResponse<T> {
    builder.method = HttpMethod.Get
    return apiResponseOf { request(builder) }
}

suspend inline fun <reified T> HttpClient.getApiResponse(
    block: HttpRequestBuilder.() -> Unit,
): ApiResponse<T> = getApiResponse(HttpRequestBuilder().apply(block))
