package com.sumera.argallery.domain.base

import com.sumera.argallery.tools.log.ErrorLogger
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseSingler<T> {

    abstract protected fun create(): Single<T>

    open fun execute(): Single<T> {
        return create()
                .doOnError { e -> ErrorLogger.log(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}