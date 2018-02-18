package com.sumera.argallery.domain.datasource

import com.sumera.argallery.data.store.ui.datasource.CurrentDataSourceStore
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.domain.base.BaseCompletabler
import io.reactivex.Completable
import javax.inject.Inject

class SetCurrentDataSourceCompletabler @Inject constructor(
        private val currentDataSourceStore: CurrentDataSourceStore
) : BaseCompletabler() {

    lateinit var dataSourceType: DataSourceType

    fun init(dataSourceType: DataSourceType): SetCurrentDataSourceCompletabler {
        this.dataSourceType = dataSourceType
        return this
    }

    override fun create(): Completable {
        return Completable.fromCallable {
            currentDataSourceStore.changeDataSource(dataSourceType)
        }
    }
}
