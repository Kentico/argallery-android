package com.sumera.argallery.domain.datasource

import com.sumera.argallery.data.store.ui.datasource.CurrentDataSourceStore
import com.sumera.argallery.domain.base.BaseCompletabler
import io.reactivex.Completable
import javax.inject.Inject

class LoadMoreFromDataSourceCompletabler @Inject constructor(
        private val currentDataSourceStore: CurrentDataSourceStore
) : BaseCompletabler() {

    override fun create(): Completable {
        return currentDataSourceStore.loadMore()
    }
}