package com.example.hubapp

import android.util.Log

object LogHelper {
    private const val GLOBAL_TAG = "AppHub"

    fun logDebug(tag: String, message: String) {
        Log.d("$GLOBAL_TAG-$tag", message)
    }

    fun logInfo(tag: String, message: String) {
        Log.i("$GLOBAL_TAG-$tag", message)
    }

    fun logWarning(tag: String, message: String) {
        Log.w("$GLOBAL_TAG-$tag", message)
    }

    fun logVerbose(tag: String, message: String) {
        Log.v("$GLOBAL_TAG-$tag", message)
    }

    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$GLOBAL_TAG-$tag", message, throwable)
    }

    fun appStart(appName: String) {
        logInfo(appName, "Aplicativo $appName iniciado.")
    }

    fun appStop(appName: String) {
        logInfo(appName, "Aplicativo $appName encerrado.")
    }
}