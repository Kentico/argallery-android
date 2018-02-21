package com.sumera.argallery.data.store.ui

import com.sumera.argallery.data.store.ui.model.Filter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterStore @Inject constructor() {

    private val currentFilterSubject = BehaviorSubject.createDefault(Filter.createDefault())

    fun getCurrentFilter(): Filter {
        return currentFilterSubject.value
    }

    fun getCurrentFilterSingle(): Single<Filter> {
        return getCurrentFilterObservable().take(1).firstOrError()
    }

    fun getCurrentFilterObservable(): Observable<Filter> {
        return currentFilterSubject.hide().distinctUntilChanged()
    }

    fun setCurrentFilter(filter: Filter) {
        currentFilterSubject.onNext(filter)
    }
}