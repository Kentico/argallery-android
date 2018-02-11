package com.sumera.argallery.tools.log

import com.github.ajalt.timberkt.Timber

object ErrorLogger {

    fun log(e: Throwable) {
        Timber.e(e)
    }
}