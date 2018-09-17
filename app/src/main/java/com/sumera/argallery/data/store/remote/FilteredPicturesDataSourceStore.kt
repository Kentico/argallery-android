package com.sumera.argallery.data.store.remote

import com.kenticocloud.delivery_core.interfaces.item.common.IQueryParameter
import com.kenticocloud.delivery_core.models.common.Filters
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

    override fun createQueryParams(): List<IQueryParameter> {
        val filter = filterStore.getCurrentFilter()
        val parameters = mutableListOf(
                Filters.GreaterThanOrEqualFilter("elements.price", filter.minPrice.toString()),
                Filters.LessThanOrEqualFilter("elements.price", filter.maxPrice.toString()),
                Filters.GreaterThanOrEqualFilter("elements.year", filter.minYear.toString()),
                Filters.LessThanOrEqualFilter("elements.year", filter.maxYear.toString())
        )
        val categoryFilter = createFilterFromSelectedCategories(filter)
        parameters.add(categoryFilter)

        return parameters
    }

    private fun createFilterFromSelectedCategories(filter: Filter): IQueryParameter {
        val filteredCategories = mutableListOf<String>()
        if (filter.firstCategoryEnabled) {
            filteredCategories.add("is_animal_picture")
        }
        if (filter.secondCategoryEnabled) {
            filteredCategories.add("is_nature_picture")
        }

        return Filters.AnyFilter("elements.categories", filteredCategories)
    }

    private fun subscribeToFilterChanges() {
        filterStore.getCurrentFilterObservable()
                .subscribe { reload() }
    }
}