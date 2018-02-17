package com.sumera.argallery.domain.base

import com.sumera.argallery.tools.log.ErrorLogger
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseCompletabler {

    abstract protected fun create(): Completable

    open fun execute(): Completable {
        return create()
                .doOnError { e -> ErrorLogger.log(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}