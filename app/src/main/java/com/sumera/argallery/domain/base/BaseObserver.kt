package com.sumera.argallery.domain.base

import com.sumera.argallery.tools.log.ErrorLogger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseObserver<T> {

	abstract protected fun create(): Observable<T>

	open fun execute(): Observable<T> {
		return create()
				.doOnError { e -> ErrorLogger.log(e) }
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
	}
}
