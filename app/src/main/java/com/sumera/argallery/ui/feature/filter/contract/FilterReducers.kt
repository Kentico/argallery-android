package com.sumera.argallery.ui.feature.filter.contract

import com.sumera.argallery.data.store.ui.model.Filter
import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class FilterReducers : MviStateReducer<FilterState>

data class SetCurrentFilter(val filter: Filter) : FilterReducers() {
    override fun reduce(oldState: FilterState): FilterState {
        return oldState.copy(currentFilter = filter)
    }
}

data class SetYearRangeReducer(val minYear: Int, val maxYear: Int) : FilterReducers() {
    override fun reduce(oldState: FilterState): FilterState {
        val newFilter = oldState.currentFilter.copy(minYear = minYear, maxYear = maxYear)
        return oldState.copy(currentFilter = newFilter)
    }
}

data class SetPriceRangeReducer(val minPrice: Int, val maxPrice: Int) : FilterReducers() {
    override fun reduce(oldState: FilterState): FilterState {
        val newFilter = oldState.currentFilter.copy(minPrice = minPrice, maxPrice = maxPrice)
        return oldState.copy(currentFilter = newFilter)
    }
}

data class SetFirstCategoryState(val isEnabled: Boolean) : FilterReducers() {
    override fun reduce(oldState: FilterState): FilterState {
        val newFilter = oldState.currentFilter.copy(firstCategoryEnabled = isEnabled)
        return oldState.copy(currentFilter = newFilter)
    }
}

data class SetSecondCategoryState(val isEnabled: Boolean) : FilterReducers() {
    override fun reduce(oldState: FilterState): FilterState {
        val newFilter = oldState.currentFilter.copy(secondCategoryEnabled = isEnabled)
        return oldState.copy(currentFilter = newFilter)
    }
}

data class SetThirdCategoryState(val isEnabled: Boolean) : FilterReducers() {
    override fun reduce(oldState: FilterState): FilterState {
        val newFilter = oldState.currentFilter.copy(thirdCategoryEnabled = isEnabled)
        return oldState.copy(currentFilter = newFilter)
    }
}