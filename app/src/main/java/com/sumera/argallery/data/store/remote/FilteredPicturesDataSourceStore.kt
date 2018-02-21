package com.sumera.argallery.data.store.remote

import com.sumera.argallery.data.store.ui.FilterStore
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.tools.log.ErrorLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilteredPicturesDataSourceStore @Inject constructor(
        private val kenticoStore: KenticoStore,
        private val errorLogger: ErrorLogger,
        private val filterStore: FilterStore
) : AllPicturesDataSourceStore(kenticoStore, errorLogger) {

    init {
        subscribeToFilterChanges()
    }

    override val dataSourceType = DataSourceType.FILTERED

    override fun createQueryParams(): Map<String, String> {
        val filter = filterStore.getCurrentFilter()
        return mapOf(
                "elements.price[gte]" to filter.minPrice.toString(),
                "elements.price[lte]" to filter.maxPrice.toString()
        )
    }

    private fun subscribeToFilterChanges() {
        filterStore.getCurrentFilterObservable()
                .subscribe { reload() }
    }
}