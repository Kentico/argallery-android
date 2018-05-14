package com.sumera.argallery.data.store.remote

import com.sumera.argallery.data.store.ui.FilterStore
import com.sumera.argallery.data.store.ui.datasource.model.DataSourceType
import com.sumera.argallery.data.store.ui.model.Filter
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
                "elements.price[lte]" to filter.maxPrice.toString(),
                "elements.year[gte]" to filter.minYear.toString(),
                "elements.year[lte]" to filter.maxYear.toString(),
                "elements.categories[any]" to createArrayFromSelectedCategories(filter)
        )
    }

    private fun subscribeToFilterChanges() {
        filterStore.getCurrentFilterObservable()
                .subscribe { reload() }
    }

    private fun createArrayFromSelectedCategories(filter: Filter): String {
        val categories = mutableListOf<String>()

        if (filter.firstCategoryEnabled) {
            categories.add("is_animal_picture")
        }

        if (filter.secondCategoryEnabled) {
            categories.add("is_nature_picture")
        }

        return categories.joinToString(separator = ",")
    }
}