package com.sumera.argallery.domain.datasource

import com.sumera.argallery.data.store.ui.datasource.CurrentDataSourceStore
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.domain.base.BaseObserver
import io.reactivex.Observable
import javax.inject.Inject

class GetCurrentDataSourceStateObserver @Inject constructor(
        private val currentDataSourceStore: CurrentDataSourceStore
) : BaseObserver<PicturesWithLoadingState>() {

    override fun create(): Observable<PicturesWithLoadingState> {
        return currentDataSourceStore.getPicturesWithLoadingStateObservable()
    }
}