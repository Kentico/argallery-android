package com.sumera.argallery.domain.base

import com.sumera.argallery.tools.log.ErrorLogger
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseMaybeInteractor<T> {

    abstract fun create(): Maybe<T>

    open fun execute(): Maybe<T> {
        return create()
                .doOnError { e -> ErrorLogger.log(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}