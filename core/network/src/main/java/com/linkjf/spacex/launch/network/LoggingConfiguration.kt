package com.linkjf.spacex.launch.network

import android.content.Context
import android.content.pm.ApplicationInfo
import okhttp3.logging.HttpLoggingInterceptor

object LoggingConfiguration {
    fun createHttpLoggingInterceptor(context: Context): HttpLoggingInterceptor {
        val isDebug = isDebugBuild(context)

        return HttpLoggingInterceptor().apply {
            level = getLoggingLevel(isDebug)
        }
    }

    private fun isDebugBuild(context: Context): Boolean = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    private fun getLoggingLevel(isDebug: Boolean): HttpLoggingInterceptor.Level =
        if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
}
