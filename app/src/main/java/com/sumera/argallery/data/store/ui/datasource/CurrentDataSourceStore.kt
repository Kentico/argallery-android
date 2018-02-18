package com.sumera.argallery.data.store.ui.datasource

import com.sumera.argallery.data.store.persistence.FavouritePicturesDataSourceStore
import com.sumera.argallery.data.store.remote.AllPicturesDataSourceStore
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.PicturesWithLoadingState
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentDataSourceStore @Inject constructor(
        private val allPicturesDataSourceStore: AllPicturesDataSourceStore,
        private val favouritePicturesDataSourceStore: FavouritePicturesDataSourceStore
) {

    private val currentDataSourceSubject = BehaviorSubject.createDefault(getDataSourceByType(DataSourceType.FAVOURITES))

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
            else -> throw NotImplementedError()
        }
    }
}