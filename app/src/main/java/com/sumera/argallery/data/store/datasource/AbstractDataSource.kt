package com.sumera.argallery.data.store.datasource

import com.sumera.argallery.data.store.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

abstract class AbstractDataSource {

    abstract val dataSourceType: DataSourceType

    abstract fun reload()

    abstract fun loadMore()

    val picturesWithLoadingStateSingle: Single<PicturesWithLoadingState>
        get() = Single.fromObservable(picturesWithLoadingStateObservable.take(1))

    val picturesWithLoadingStateObservable: Observable<PicturesWithLoadingState>
        get() = picturesWithLoadingStateSubject.hide()

    private val picturesWithLoadingStateSubject = BehaviorSubject.createDefault(PicturesWithLoadingState.createDefault())

    fun changeState(picturesWithLoadingState: PicturesWithLoadingState) {
        picturesWithLoadingStateSubject.onNext(picturesWithLoadingState)
    }
}