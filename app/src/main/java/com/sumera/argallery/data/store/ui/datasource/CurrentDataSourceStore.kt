package com.sumera.argallery.data.store.ui.datasource

import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.remote.AllPicturesDataSourceStore
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CurrentDataSourceStore @Inject constructor(
        private val allPicturesDataSourceStore: AllPicturesDataSourceStore
) {

    val currentDataSourceObservable: Observable<AbstractDataSource>
        get() = currentDataSourceSubject.hide()

    private val currentDataSourceSubject = BehaviorSubject.createDefault(getDataSourceByType(DataSourceType.ALL))

    fun changeDataSource(dataSourceType: DataSourceType) {
        currentDataSourceSubject.onNext(getDataSourceByType(dataSourceType))
    }

    private fun getDataSourceByType(dataSource: DataSourceType): AbstractDataSource {
        return allPicturesDataSourceStore
//        return when(dataSource) {
//            DataSourceType.ALL -> allPicturesDataSourceStore
//            else -> throw NotImplementedError()
//        }
    }
}