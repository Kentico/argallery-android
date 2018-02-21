package com.sumera.argallery.ui.feature.filter.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class FilterActions : MviAction<FilterState>

object OnCloseAreaClicked : FilterActions()

object OnCloseButtonClicked : FilterActions()

object OnBackButtonClicked : FilterActions()

object OnResetButtonClicked : FilterActions()

data class OnYearRangeChanged(
        val minYear: Int,
        val maxYear: Int
) : FilterActions()

data class OnPriceRangeChanged(
        val minPrice: Int,
        val maxPrice: Int
) : FilterActions()

data class OnFirstCategoryStateChanged(
        val isEnabled: Boolean
) : FilterActions()

data class OnSecondCategoryStateChanged(
        val isEnabled: Boolean
) : FilterActions()

data class OnThirdCategoryStateChanged(
        val isEnabled: Boolean
) : FilterActions()