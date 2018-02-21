package com.sumera.argallery.data.store.ui.datasource

import com.sumera.argallery.data.store.persistence.FavouritePicturesDataSourceStore
import com.sumera.argallery.data.store.remote.AllPicturesDataSourceStore
import com.sumera.argallery.data.store.remote.FilteredPicturesDataSourceStore
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import com.sumera.argallery.tools.DEFAULT_DATA_SOURCE
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentDataSourceStore @Inject constructor(
        private val allPicturesDataSourceStore: AllPicturesDataSourceStore,
        private val favouritePicturesDataSourceStore: FavouritePicturesDataSourceStore,
        private val filteredPicturesDataSourceStore: FilteredPicturesDataSourceStore
) {

    private val currentDataSourceSubject = BehaviorSubject.createDefault(getDataSourceByType(DEFAULT_DATA_SOURCE))

    fun loadMore(): Completable {
        return currentDataSourceSubject.hide()
                .take(1)
                .doOnNext { it.loadMore() }
                .ignoreElements()
    }

    fun getCurrentDataSourceTypeObservable(): Observable<DataSourceType> {
        return currentDataSourceSubject.hide()
                .map { it.dataSourceType }
    }

    fun getPicturesWithLoadingStateObservable(): Observable<PicturesWithLoadingState> {
        return currentDataSourceSubject.hide()
                .switchMap { it.picturesWithLoadingStateObservable }
    }

    fun changeDataSource(dataSourceType: DataSourceType) {
        currentDataSourceSubject.onNext(getDataSourceByType(dataSourceType))
    }

    private fun getDataSourceByType(dataSource: DataSourceType): AbstractDataSource {
        return when(dataSource) {
            DataSourceType.ALL -> allPicturesDataSourceStore
            DataSourceType.FAVOURITES -> favouritePicturesDataSourceStore
            DataSourceType.FILTERED -> filteredPicturesDataSourceStore
        }
    }
}