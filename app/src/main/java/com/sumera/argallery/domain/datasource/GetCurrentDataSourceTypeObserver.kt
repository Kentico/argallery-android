package com.sumera.argallery.domain.datasource

import com.sumera.argallery.data.store.datasource.CurrentDataSourceStore
import com.sumera.argallery.data.store.datasource.model.DataSourceType
import com.sumera.argallery.domain.base.BaseObserver
import io.reactivex.Observable
import javax.inject.Inject

class GetCurrentDataSourceTypeObserver @Inject constructor(
        private val currentDataSourceStore: CurrentDataSourceStore
) : BaseObserver<DataSourceType>() {

    override fun create(): Observable<DataSourceType> {
        return currentDataSourceStore.currentDataSourceObservable
                .map { it.dataSourceType }
    }
}
