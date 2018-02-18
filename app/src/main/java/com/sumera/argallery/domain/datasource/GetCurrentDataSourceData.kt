package com.sumera.argallery.domain.datasource

import com.sumera.argallery.data.store.ui.datasource.CurrentDataSourceStore
import com.sumera.argallery.data.store.ui.model.Picture
import com.sumera.argallery.domain.base.BaseObserver
import io.reactivex.Observable
import javax.inject.Inject

class GetCurrentDataSourceData @Inject constructor(
        private val currentDataSourceStore: CurrentDataSourceStore
) : BaseObserver<List<Picture>>() {

    override fun create(): Observable<List<Picture>> {
        return currentDataSourceStore.currentDataSourceObservable
                .flatMap { it.picturesWithLoadingStateObservable }
                .map { it.pictures }
    }
}