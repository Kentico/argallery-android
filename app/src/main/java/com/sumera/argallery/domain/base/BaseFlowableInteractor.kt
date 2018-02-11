package com.sumera.argallery.domain.base

import com.sumera.argallery.tools.log.ErrorLogger
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseFlowableInteractor<T> {

    abstract protected fun create(): Flowable<T>

    open fun execute(): Flowable<T> {
        return create()
                .doOnError { e -> ErrorLogger.log(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
