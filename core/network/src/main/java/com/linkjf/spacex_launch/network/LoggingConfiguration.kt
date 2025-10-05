package com.linkjf.spacex_launch.network

import android.content.Context
import okhttp3.logging.HttpLoggingInterceptor

object LoggingConfiguration {
    
    fun createHttpLoggingInterceptor(context: Context): HttpLoggingInterceptor {
        val isDebug = isDebugBuild(context)
        
        return HttpLoggingInterceptor().apply {
            level = getLoggingLevel(isDebug)
        }
    }
    
    private fun isDebugBuild(context: Context): Boolean {
        return context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0
    }
    
    private fun getLoggingLevel(isDebug: Boolean): HttpLoggingInterceptor.Level {
        return if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}
