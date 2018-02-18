package com.sumera.argallery.tools.extensions

import io.reactivex.Observable
import io.reactivex.functions.Function3

object Rx {

    fun <F, S, T> combineLatestTriple(
            firstObservable: Observable<F>,
            secondObservable: Observable<S>,
            thirdObservable: Observable<T>
    ): Observable<Triple<F, S, T>> {
        return Observable.combineLatest(
                firstObservable,
                secondObservable,
                thirdObservable,
                Function3 { f:F, s:S, t:T -> Triple(f, s, t) }
        )
    }
}