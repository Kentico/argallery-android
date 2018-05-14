package com.sumera.argallery.data.store.ui.model

import com.sumera.argallery.tools.FILTER_MAX_PRICE
import com.sumera.argallery.tools.FILTER_MAX_YEAR
import com.sumera.argallery.tools.FILTER_MIN_PRICE
import com.sumera.argallery.tools.FILTER_MIN_YEAR

data class Filter(
        val minPrice: Int,
        val maxPrice: Int,
        val minYear: Int,
        val maxYear: Int,
        val firstCategoryEnabled: Boolean,
        val secondCategoryEnabled: Boolean
) {
    companion object {
        fun createDefault(): Filter {
            return Filter(
                    minPrice = FILTER_MIN_PRICE,
                    maxPrice = FILTER_MAX_PRICE,
                    minYear = FILTER_MIN_YEAR,
                    maxYear = FILTER_MAX_YEAR,
                    firstCategoryEnabled = true,
                    secondCategoryEnabled = true
            )
        }
    }
}

