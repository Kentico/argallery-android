package com.sumera.argallery.tools.log

import com.github.ajalt.timberkt.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorLogger @Inject constructor() {

    companion object {
        fun log(e: Throwable) {
            Timber.e(e)
        }
    }

    fun logException(e: Throwable) {
        ErrorLogger.log(e)
    }
}