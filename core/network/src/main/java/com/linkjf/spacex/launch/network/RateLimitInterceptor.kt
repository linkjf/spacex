package com.linkjf.spacex.launch.network

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateLimitInterceptor
@Inject
constructor(
    @ApplicationContext private val context: Context,
) : Interceptor {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 429) {
            val retryAfter = response.header("Retry-After")
            val retryAfterSeconds = retryAfter?.toIntOrNull() ?: DEFAULT_RETRY_AFTER_SECONDS

            val resetTimestamp = System.currentTimeMillis() + (retryAfterSeconds * 1000L)
            saveRateLimitInfo(resetTimestamp, retryAfterSeconds)

            android.util.Log.w(
                "RateLimitInterceptor",
                "Rate limit hit (429): retry after $retryAfterSeconds seconds",
            )
        }

        return response
    }

    private fun saveRateLimitInfo(
        resetTimestamp: Long,
        retryAfterSeconds: Int,
    ) {
        prefs.edit().apply {
            putLong(KEY_RESET_TIMESTAMP, resetTimestamp)
            putInt(KEY_RETRY_AFTER_SECONDS, retryAfterSeconds)
            putLong(KEY_LAST_429_TIME, System.currentTimeMillis())
            apply()
        }
    }

    fun getRateLimitInfo(): RateLimitInfo? {
        val resetTimestamp = prefs.getLong(KEY_RESET_TIMESTAMP, 0L)
        if (resetTimestamp == 0L) return null

        val now = System.currentTimeMillis()
        if (now >= resetTimestamp) {
            clearRateLimitInfo()
            return null
        }

        val remainingSeconds = ((resetTimestamp - now) / 1000).toInt()
        return RateLimitInfo(
            resetTimestamp = resetTimestamp,
            remainingSeconds = remainingSeconds,
        )
    }

    fun clearRateLimitInfo() {
        prefs.edit().apply {
            remove(KEY_RESET_TIMESTAMP)
            remove(KEY_RETRY_AFTER_SECONDS)
            remove(KEY_LAST_429_TIME)
            apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "rate_limit_prefs"
        private const val KEY_RESET_TIMESTAMP = "reset_timestamp"
        private const val KEY_RETRY_AFTER_SECONDS = "retry_after_seconds"
        private const val KEY_LAST_429_TIME = "last_429_time"
        private const val DEFAULT_RETRY_AFTER_SECONDS = 36000
    }

    data class RateLimitInfo(
        val resetTimestamp: Long,
        val remainingSeconds: Int,
    )
}
