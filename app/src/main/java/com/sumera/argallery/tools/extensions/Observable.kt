package com.sumera.argallery.tools.extensions

import android.support.v7.util.DiffUtil
import com.sumera.argallery.ui.common.diffutil.DiffUtilItem
import com.sumera.argallery.ui.common.diffutil.GenericDiffUtilCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T, R> Observable<T>.scanMap(initialValue: T, func2: (T, T) -> R): Observable<R> {
    return this.startWith(initialValue)
            .buffer(2, 1)
            .filter { it.size >= 2 }
            .map { func2.invoke(it[0], it[1]) }
}

fun <T : DiffUtilItem> Observable<List<T>>.calculateDiffUtilResult(): Observable<Pair<List<T>, DiffUtil.DiffResult>> {
    return scanMap(listOf(), { old, new -> Pair(old, new) })
            .concatMap { (old, new) ->
                GenericDiffUtilCallback.calculate(old, new)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { Pair(new, it)}
            }
}