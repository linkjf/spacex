package com.linkjf.spacex.launch.network.exceptions

/**
 * Custom exception for HTTP errors
 */
sealed class HttpException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    /**
     * HTTP 429 Too Many Requests - Rate limiting
     */
    class TooManyRequests(
        val retryAfterSeconds: Int? = null,
        message: String = "Too many requests. Please try again later.",
    ) : HttpException(message)

    /**
     * Generic HTTP error
     */
    class HttpError(
        val statusCode: Int,
        message: String,
    ) : HttpException(message)

    /**
     * Network connectivity error
     */
    class NetworkError(
        message: String = "Network error. Please check your connection.",
        cause: Throwable? = null,
    ) : HttpException(message, cause)
}
